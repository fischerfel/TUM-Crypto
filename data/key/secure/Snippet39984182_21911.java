private static final String AWS_S3_REGION_NAME = "us-west-2";
private static final String AWS_S3_SERVICE_NAME = "s3";
private static final String AWS_S3_KEY_ID = "<My Key ID>";
private static final String AWS_S3_KEY_SECRET = "<My secret>";
private static final String BUCKET_NAME = "<My Bucket Name>";
private static final String DOCUMENT_OBJECT_KEY = "MyTest1.txt";

public static void main(String[] args) {
    String timeStamp = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").format(new java.util.Date());
    Response response = null;
    byte[] signatureKey = null;
    try {
        signatureKey = getSignatureKey(AWS_S3_KEY_ID, timeStamp, "us-west-2", "s3");
        response = getRestFileFromS3(timeStamp, IOUtils.toString(signatureKey, StandardCharsets.UTF_8.displayName()));
    } catch (Exception e) {
        e.printStackTrace();
    }
    System.out.println(response.getHeaders());
}

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

static Response getRestFileFromS3(String date, String signatureKey) {
    Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );

    WebTarget webTarget = client.target("https://s3-us-west-2.amazonaws.com/")
            .path(BUCKET_NAME)
            .path(DOCUMENT_OBJECT_KEY);

    Invocation.Builder invocationBuilder =  webTarget
            .request(MediaType.APPLICATION_JSON)
            .header("Host", BUCKET_NAME+".s3.amazonaws.com")
            .header("Date", date)
            .header("Authorization", signatureKey);
    return invocationBuilder.get();
}
