@Named
@Stateless
@LocalBean
public class CustomerService {
    @PersistenceContext(unitName = "MovieProject-ejbPU")
    private EntityManager em;

    private String username;
    private String password;

    //getters and setters

    public void validateEmail() {
        Properties serverConfig = new Properties();
        serverConfig.put("mail.smtp.host", "localhost");
        serverConfig.put("mail.smtp.auth", "true");
        serverConfig.put("mail.smtp.port", "25");

        try {
            Session session = Session.getInstance(serverConfig, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("<ACCOUNT>","<PASSWORD>");
                }
            });
            MimeMessage message = new MimeMessage( session );
            message.setFrom(new InternetAddress("accounts@minimalcomputers.com","VideoPile"));
            message.addRecipient(
                Message.RecipientType.TO, new InternetAddress(username)
            );
            message.setSubject("Welcome to VideoPile!");
            message.setContent("<p>Welcome to VideoPile, Please verify your email.</p><p>" + verifierKey + "</p>", "text/html; charset=utf-8"); //verifierKey is what I'm trying to get from AccountVerifierBean.
            Transport.send( message );
        }
        catch (MessagingException ex){
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception e) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public String encrypt(String password) {
        try {
            return new     String(Base64.encode(MessageDigest.getInstance("SHA").digest(password.getBytes())));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
