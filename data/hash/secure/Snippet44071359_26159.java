public static String hashIt(String msg, String key) {

    MessageDigest m = null;
    String hashText = null;
    byte[] actualKeyBytes =  TripleDES.hexStringToBytes(key);
    try {
        m = MessageDigest.getInstance("SHA-256");
        m.update(actualKeyBytes, 0, actualKeyBytes.length);
        try {
            m.update(msg.getBytes("UTF-8"), 0, msg.length());
        } catch (UnsupportedEncodingException ex) {

        }
        hashText = TripleDES.bytesToHexString( m.digest() ); //new BigInteger(1, m.digest()).toString(16);

    } catch (NoSuchAlgorithmException ex) {

    }
    return hashText;
}
