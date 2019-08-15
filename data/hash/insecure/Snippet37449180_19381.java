public class MyLoginModule extends DatabaseServerLoginModule {

   private static final Logger LOGGER = Logger.getLogger(MyLoginModule.class);
   @Override
   protected boolean validatePassword(String inputPassword, String expectedPassword) {

    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        LOGGER.error(e.getMessage());
    }
    byte[] passwordBytes = inputPassword.getBytes();
    byte[] hash = md.digest(passwordBytes);
    String passwordHash =
            org.jboss.security.Base64Utils.tob64(hash);
    LOGGER.info(expectedPassword.equals(passwordHash));
    return expectedPassword.equals(passwordHash);
   }
}
