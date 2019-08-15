private static final String QUERY = "GET\n"+
        "sellercentral.amazon.com\n"+
        "/gp/mws/registration/register.html\n"+
        "AWSAccessKeyId=AKIAFJPPO5KLY6G4XO7Q&SignatureMethod=HmacSHA256&SignatureVersion=2&id=1014f5ad-c359-4e86-8e50-bb8f8e431a9e&returnPathAndParameters=%2Forders%2FlistRecentOrders.jsp%3FsessionId%3D123";


    public static void main(String[] args) throws Exception {
         System.out.println(encode("aaa", QUERY));
    }

public static String encode(String key, String data) throws Exception {
      Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
      sha256_HMAC.init(secret_key);
      return Base64.encodeBase64String(sha256_HMAC.doFinal(data.getBytes()));
}
