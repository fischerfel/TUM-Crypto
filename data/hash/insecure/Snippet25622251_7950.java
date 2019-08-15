@Stateless
@Path( "/user" )
public class UserResource {

    private static final Logger     LOG = Logger.getLogger( "xyz" );

    @PersistenceContext( unitName = "xyz" )
    private EntityManager em;

    @Resource
    EJBContext ejbContext;

    @Resource
    ManagedExecutorService managedExecutorService;

    @EJB
    private EmailBean       emailBean;


    @PUT
    @Path( "/create/{type: [0-9]}" )
    @Produces( "text/plain" )
    public void create( @Suspended final AsyncResponse asyncResp, @Context final HttpHeaders hh, @PathParam("type") final int type ) throws NoSuchAlgorithmException {

        if( type == User.USER_TYPE_EMAIL ) {

            LOG.log( Level.INFO, "REST PUT: user/create/email-user, headers:{0}", ProjectStatics.buildHeaderString( hh.getRequestHeaders() ) );

            String email    = hh.getHeaderString( User.REST_HEADER_USER_EMAIL );
            String authHash = hh.getHeaderString( User.REST_HEADER_AUTH_HASH );
            UserCreateRunnable ucr = new UserCreateRunnable( asyncResp, type, email, authHash );
            managedExecutorService.execute( ucr );

        }
        else
            asyncResp.resume( Response.status( Response.Status.BAD_REQUEST ).build() );

    }


    private final class UserCreateRunnable implements Runnable {

        private final int type;

        private String email;

        private final String authHash;

        private final AsyncResponse asynResp;


        UserCreateRunnable( AsyncResponse asynResp, int type, String email, String authHash ) {
            this.type = type;
            this.email = email;
            this.authHash = authHash;
            this.asynResp = asynResp;
        }


        @Override
        public void run() {

            LOG.log( Level.INFO, "UCR.run: ar={0}, type={1}, email={2}, authHash={3}, em={4}, ejbCtx={5}, emailBean={6}", new Object[] {asynResp,type,email,authHash,em,ejbContext,emailBean} );

            // check email
            if( email == null  ||  email.isEmpty()  ||  !email.contains( "@" )  ||  !email.contains( "." ) ) {

                LOG.log( Level.WARNING, "UCR.run: AR:{0}", asynResp );
                asynResp.resume( Response.status( Response.Status.BAD_REQUEST ).entity( Integer.toString( User.REST_RESPONSE_WRONG_EMAIL ) ).build() );
                return;
            }


            // check pwd hash (authHash)
            if( authHash == null  ||  authHash.length() != AuthenticationFilter.DIGEST_LENGTH ) {
                LOG.log( Level.WARNING, "create user: wrong auth={0}", authHash );
                asynResp.resume( Response.status( Response.Status.BAD_REQUEST ).entity( Integer.toString( User.REST_RESPONSE_WRONG_AUTH ) ).build() );
                return;
            }

            email = email.toLowerCase();
            try {
                TypedQuery<User> tq = em.createNamedQuery( "User.findByEmail", User.class );
                    tq.setParameter( "email", email );
                User user = tq.getSingleResult();
                LOG.log( Level.WARNING, "create user: doublicate email={0}", email );
                asynResp.resume( Response.status( Response.Status.BAD_REQUEST ).entity( Integer.toString( User.REST_RESPONSE_OCCUPIED_EMAIL ) ).build() );
                return;
            }
            catch( NoResultException nre ) {
                // no user with same email found -> progesss registration...
            }


            // send confirmation email
            try {

                LOG.log( Level.FINE, "UCR.run: everything fine, process..." );
                // create & save user
                User user = new User();
                    user.setEmail( email );
                    user.setAuth( authHash );
                LOG.log( Level.FINE, "UCR.run: User created, persist..." );
                em.persist( user );
                LOG.log( Level.FINE, "UCR.run: ...persisted!" );

                MessageDigest md = MessageDigest.getInstance( "MD5" );
                String key = DatatypeConverter.printHexBinary( md.digest( (System.currentTimeMillis()+email).getBytes() ) );

                UserEmailRegistration uer = new UserEmailRegistration();
                    uer.setUser( user );
                    uer.setDate( new Date( System.currentTimeMillis() ) );
                    uer.setKey( key );
                LOG.log( Level.FINE, "UCR.run: UserEmailReg created, persist..." );
                em.persist( uer );
                LOG.log( Level.FINE, "UCR.run: ...persisted!" );
                LOG.log( Level.FINE, "UCR.run: flush em..." );
                em.flush();
                LOG.log( Level.FINE, "UCR.run: ...flushed!" );

                // send registration email
                LOG.log( Level.FINE, "UCR.run: persisted User, send email..." );
                emailBean.sendRegistrationEmail( uer );

                LOG.log( Level.FINE, "UCR.run: everything done, resume..." );
                asynResp.resume( Response.ok( Integer.toString( user.getId() ) ).build() );

            }
            catch( MessagingException ex ) {
                LOG.log( Level.WARNING, "UCR.run: ", ex );
                ejbContext.setRollbackOnly();
                asynResp.resume( Response.status( Response.Status.BAD_REQUEST ).entity( User.REST_RESPONSE_WRONG_EMAIL ).build() );
            }
            catch( NoSuchAlgorithmException ex ) {
                LOG.log( Level.WARNING, "UCR.run: ", ex );
                ejbContext.setRollbackOnly();
                asynResp.resume( Response.status( Response.Status.INTERNAL_SERVER_ERROR ).build() );
            }
        }
    }
}
