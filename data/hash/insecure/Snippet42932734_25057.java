public static String buildMD5PasswordDigest(String nonce, String created,
        String password)  {
    String passwordDigest = null;

    try {
        MessageDigest sha1 = MessageDigest.getInstance("MD5");
        sha1.update(nonce.getBytes("UTF-8"));
        sha1.update(created.getBytes("UTF-8"));
        byte[] digest = sha1
                .digest(password.getBytes("UTF-8"));

         //converting byte array to Hexadecimal String 
         StringBuilder sb = new StringBuilder(2 * digest.length); 
         for(byte b : digest){ sb.append(String.format("%02x", b&0xff)); } 
         passwordDigest = sb.toString();
        sha1.reset();
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        LogServiceImpl.logError(e);
    }

    return passwordDigest;
}
