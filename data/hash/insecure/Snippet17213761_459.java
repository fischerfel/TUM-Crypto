private Logins l;
private String password;

public String hashPass(String pass) throws NoSuchAlgorithmException {
    MessageDigest mdEnc = MessageDigest.getInstance("MD5"); 
    mdEnc.update(password.getBytes(), 0, password.length());
    String md5 = new BigInteger(1, mdEnc.digest()).toString(16); // Encrypted 
    return md5;
}
