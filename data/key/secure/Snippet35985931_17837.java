    String secretKey = "{mysecretkey}";
    String dateStamp = "20160314";
    String regionName = "ap-southeast-1";
    String serviceName = "execute-api";

    byte[] signature = getSignatureKey(secretKey, dateStamp, regionName, serviceName);
    System.out.println("Signature : " + Hex.encodeHexString(signature));

    static byte[] HmacSHA256(String data, byte[] key) throws Exception  {
         String algorithm="HmacSHA256";
         Mac mac = Mac.getInstance(algorithm);
         mac.init(new SecretKeySpec(key, algorithm));
         return mac.doFinal(data.getBytes("UTF8"));
    }

    static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception  {
         byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
         byte[] kDate    = HmacSHA256(dateStamp, kSecret);
         byte[] kRegion  = HmacSHA256(regionName, kDate);
         byte[] kService = HmacSHA256(serviceName, kRegion);
         byte[] kSigning = HmacSHA256("aws4_request", kService);
         return kSigning;
    }
