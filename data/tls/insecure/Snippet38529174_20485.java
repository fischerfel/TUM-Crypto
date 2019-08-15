@RequestMapping(value = "/social/facebook")
@Component
public class FacebookController<FacebookApi> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FacebookController.class);

    private static final String PUBLISH_SUCCESS = "success";
    private static final String FACEBOOK = "facebook";


    @Autowired
    private ConnectionFactoryRegistry connectionFactoryRegistry;

    @Autowired
    private OAuth2Parameters oAuth2Parameters;


    @Autowired
    @Qualifier("facebookServiceProvider")
    private OAuthServiceProvider<FacebookApi> facebookServiceProvider;


    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView signin(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        System.out.println("Redirecting to Facebook.........");
        System.out.println(System.getProperty("java.home"));
        System.setProperty( "http.proxyPort", "80" );
        System.setProperty( "https.proxyPort", "80" );
        System.setProperty( "http.proxyHost", "10.68.248.98" );
        System.setProperty( "https.proxyHost", "10.68.248.98" );

        FacebookConnectionFactory facebookConnectionFactory = (FacebookConnectionFactory) connectionFactoryRegistry
                .getConnectionFactory(FACEBOOK);
        OAuth2Operations oauthOperations = facebookConnectionFactory
                .getOAuthOperations();
        oAuth2Parameters.setState("recivedfromfacebooktoken");
        String authorizeUrl = oauthOperations.buildAuthorizeUrl(
                GrantType.AUTHORIZATION_CODE, oAuth2Parameters);
        RedirectView redirectView = new RedirectView(authorizeUrl, true, true,
                true);
        System.out.println("Redirecting to Facebook again.........");
        return new ModelAndView(redirectView);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    @ResponseBody
    public void postOnWall(@RequestParam("code") String code,
            @RequestParam("state") String state, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OAuthService oAuthService = facebookServiceProvider.getService();
        System.out.println(oAuthService.getVersion());
        Verifier verifier = new Verifier(code);
        getSSL();
        Token accessToken =oAuthService.getAccessToken(Token.empty(), verifier);
        //System.out.println(accessToken);
        //System.out.println(accessToken.getToken());
        //getSSL();
        FacebookTemplate template = new FacebookTemplate(accessToken.getToken());
        //System.out.println(template);

        **// ERROR COMES HERE**

        User facebookProfile = template.userOperations().getUserProfile();
        //System.out.println(facebookProfile);
        String userId = facebookProfile.getId();
        //System.out.println(facebookProfile.getEmail());
        //System.out.println(facebookProfile.getFirstName());

        LOGGER.info("Logged in User Id : {}", userId);
        response.sendRedirect("/j_spring_security_check?j_username="+userId);
        //return "success";
    }

    @RequestMapping(value = "/callback", params = "error_reason", method = RequestMethod.GET)
    @ResponseBody
    public void error(@RequestParam("error_reason") String errorReason,
            @RequestParam("error") String error,
            @RequestParam("error_description") String description,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            LOGGER.error(
                    "Error occurred while validating user, reason is : {}",
                    errorReason);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, description);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSSL(){
        SSLContext sslContext=null;
        try {
            sslContext= SSLContext.getInstance("SSL");

            // set up a TrustManager that trusts everything
            sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    System.out.println("getAcceptedIssuers =============");
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs,
                        String authType) {
                    System.out.println("checkClientTrusted =============");
                }

                public void checkServerTrusted(X509Certificate[] certs,
                        String authType) {
                    System.out.println("checkServerTrusted =============");
                }
            } }, new SecureRandom());
        } catch (KeyManagementException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        if(sslContext!=null){
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    sslContext.getSocketFactory());

            HttpsURLConnection
            .setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String arg0, SSLSession arg1) {
                    System.out.println("hostnameVerifier =============");
                    return true;
                }
            });

        }
    }

}
