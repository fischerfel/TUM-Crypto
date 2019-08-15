private byte[] digest(String input) {
    byte[] output = null;
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        output = md.digest(input.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnsupportedEncodingException ex) {
        Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return output;
}
