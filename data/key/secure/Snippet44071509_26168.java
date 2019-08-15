public static String hashIt(String msg, String key) {
    try {
        byte[] keyBytes = key.getBytes("UTF-8");
        SecretKeySpec spec = new SecretKeySpec(keyBytes, HMAC_SHA256);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(spec);
        return TripleDES.bytesToHexString(mac.doFinal(msg.getBytes("UTF-8")));
    } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
        throw new RuntimeException(e);
    }
}
