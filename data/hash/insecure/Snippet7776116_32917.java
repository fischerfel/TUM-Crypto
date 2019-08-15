public static String MungPass(String pass) throws NoSuchAlgorithmException {
    MessageDigest m = MessageDigest.getInstance("MD5");
    byte[] data = pass.getBytes(); 
    m.update(data,0,data.length);
    BigInteger i = new BigInteger(1,m.digest());
    return String.format("%1$032X", i);
}
