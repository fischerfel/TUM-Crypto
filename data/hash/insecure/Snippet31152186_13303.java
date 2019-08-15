public class STokenUtils {
  private static final String CIPHER_INSTANCE_NAME = "AES/ECB/PKCS5Padding";

  public static final String createSToken(String siteSecret) {
    String sessionId = UUID.randomUUID().toString();
    String jsonToken = createJsonToken(sessionId);
    return encryptAes(jsonToken, siteSecret);
  }

  private static final String createJsonToken(String sessionId) {
    JsonObject obj = new JsonObject();
    obj.addProperty("session_id", sessionId);
    obj.addProperty("ts_ms", System.currentTimeMillis());
    return new Gson().toJson(obj);
  }

  private static String encryptAes(String input, String siteSecret) {
    try {
      SecretKeySpec secretKey = getKey(siteSecret);
      Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return BaseEncoding.base64Url().omitPadding().encode(cipher.doFinal(input.getBytes("UTF-8")));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String decryptAes(String input, String key) throws Exception {
    SecretKeySpec secretKey = getKey(key);
    Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    return new String(cipher.doFinal(
        BaseEncoding.base64Url().omitPadding().decode(input)), "UTF-8");
  }

  private static SecretKeySpec getKey(String siteSecret){
    try {
      byte[] key = siteSecret.getBytes("UTF-8");
      key = Arrays.copyOf(MessageDigest.getInstance("SHA").digest(key), 16);
      return new SecretKeySpec(key, "AES");
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
