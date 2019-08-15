public static byte[] getMd5OfUtf8(String text) {
    try {
        MessageDigest digest = MessageDigest.getInstance("MD5");      
        return digest.digest(text.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException ex) {
        throw new RuntimeException("No MD5 implementation? Really?");
    } catch (UnsupportedEncodingException ex) {
        throw new RuntimeException("No UTF-8 encoding? Really?");
    }
}
