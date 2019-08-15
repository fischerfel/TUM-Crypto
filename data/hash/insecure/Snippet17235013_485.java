private String hashPass(String pass) throws NoSuchAlgorithmException {
    MessageDigest mdEnc = MessageDigest.getInstance("MD5"); 
    mdEnc.update(pass.getBytes(), 0, pass.length());
    String md5 = new BigInteger(1, mdEnc.digest()).toString(8); // Encrypted 
    return md5;
}
