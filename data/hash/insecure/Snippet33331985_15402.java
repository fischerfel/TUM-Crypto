private static final String CIPHER_INSTANCE_NAME = "AES/ECB/PKCS5Padding";

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

public static void main(String [] args) throws Exception {
    //Hard coded the following to get a repeatable result
    String siteSecret = "12345678";
    String jsonToken = "{'session_id':'abf52ca5-9d87-4061-b109-334abb7e637a','ts_ms':1445705791480}";
    System.out.println(" json token: " + jsonToken);
    System.out.println(" siteSecret: " + siteSecret);
    System.out.println(" Encrypted stoken: " + encryptAes(jsonToken, siteSecret));
