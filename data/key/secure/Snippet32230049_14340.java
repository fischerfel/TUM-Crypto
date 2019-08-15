 @Component
public class S3Configuration {

         @Value("${" + AWS_ACCESS_KEY + "}")
            private String awsAccessKey;
        @Value("${" + AWS_SECRET_KEY + "}")
        private String awsSecretKey;
        @Value("${" + AWS_BUCKET_NAME + "}")
        private String awsBucketName;
        @Value("${" + AWS_ENDPOINT + "}")
        private String awsEndpoint;

        public S3Configuration(){
            try {
                byte [] secretKeyBytes = awsAccessKey.getBytes(UTF8_CHARSET);
                secretKeySpec = new SecretKeySpec(secretKeyBytes, HMAC_SHA256_ALGORITHM);
                mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
                mac.init(secretKeySpec);
            } catch(Exception e){

            }

        }
        public String sign(Map<String, String> params, String filename){
            params.put("AWSAccessKeyId", awsSecretKey);
            params.put("Timestamp", timestamp());
            String canonicalizedResource = "/" + awsBucketName + "/" + filename;
            SortedMap<String, String> sortedParamMap = new TreeMap<String, String>(params);
            String canonicalQS = canonicalize(sortedParamMap);
            String toSign = 
                    REQUEST_METHOD + "\n"
                            + awsEndpoint + "\n"
                            + canonicalizedResource + "\n"
                            + canonicalQS;
            String hmac = hmac(toSign);
            String sig = percentEncodeRfc3986(hmac);
            String url = "http://" + awsEndpoint + canonicalizedResource + "?"+canonicalQS + "&Signature=" + sig;
            return url;
        }

private String hmac(String stringToSign){
    String signature = null;
    byte[] data;
    byte[] rawHmac;
    try {
        data = stringToSign.getBytes(UTF8_CHARSET);
        rawHmac = mac.doFinal(data);
          signature = new String(Base64.encodeBase64(rawHmac));
    } catch(UnsupportedEncodingException e){
        throw new RuntimeException(UTF8_CHARSET + " is unsupported!", e);
    }
    return signature;
}
...
}
