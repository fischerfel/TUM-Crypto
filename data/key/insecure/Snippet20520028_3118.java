String sig = "";
String salt = "1386759932759";
String base_string = "11";
String key = "34ee7983-5ee6-4147-aa86-443ea062abf774493d6a-2a15-43fe-aace-e78566927585";
try {
    Mac mac = Mac.getInstance("HmacSHA1");
    SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), mac.getAlgorithm());
    mac.init(secret);
    mac.update(base_string.getBytes());
    byte[] digest = mac.doFinal(salt.getBytes());
    sig = new String(android.util.Base64.encode(digest, android.util.Base64.URL_SAFE));
} catch (Exception e) {
    e.printStackTrace();
}
AQUtility.debug("computeSignature",sig);
//nYVoP7wXhJAMGLlY4mcCwfIilMw=
