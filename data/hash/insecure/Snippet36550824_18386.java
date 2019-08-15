private String hashMe(String password) {

    try {
        MessageDigest md = MessageDigest.getInstance("SHA-1"); //could also be MD5, SHA-256 etc.
        md.reset();
        md.update(password.getBytes("UTF-8"));
        byte[] resultByte = md.digest();
        password = String.format("%01x", new java.math.BigInteger(1, resultByte));

    } catch (NoSuchAlgorithmException e) {
        //do something.
    } catch (UnsupportedEncodingException ex) {
        //do something
    }
    return password;
}
