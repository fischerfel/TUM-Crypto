public class SendSESMail {
 public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, IOException, SignatureException {
    SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
    String dateString = format.format(new Date());

    final String ENDPOINT = "https://email.us-east-1.amazonaws.com";
    final String AWS_ACCESS = "ACCESS_KEY";
    final String AWS_SECRET = "SECRET_KEY";

    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost post = new HttpPost(ENDPOINT);

    String authString = generateAuthHeader(AWS_ACCESS, AWS_SECRET, dateString);

    List<NameValuePair> formVals = new ArrayList<>();
    formVals.add(new BasicNameValuePair("Action", "SendRawEmail"));
    formVals.add(new BasicNameValuePair("Destination.ToAddresses.member.1", "bhanuka.yd@gmail.com"));
    formVals.add(new BasicNameValuePair("Message.Body.Text.Data", "I hope you see the body."));
    formVals.add(new BasicNameValuePair("Message.Subject.Data", "This is a Unique Subject"));
    formVals.add(new BasicNameValuePair("Source", "test@test.com"));

    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formVals);

    post.setHeader("Content-Type", ContentType.APPLICATION_FORM_URLENCODED.toString());
    post.setHeader("Date", dateString);
    post.setHeader("X-Amzn-Authorization", authString);

    post.setEntity(formEntity);
    HttpResponse response = httpClient.execute(post);
    response.getEntity().writeTo(System.out);

 }

 public static String generateAuthHeader(String accessKey, String secret, String dateString) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
    String authHeaderVal = "AWS3-HTTPS AWSAccessKeyId=" + accessKey + ",Algorithm=HmacSHA256,Signature=";
    authHeaderVal += generateSignature(dateString, secret);
    return authHeaderVal;
 }

 public static String generateSignature(String message, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    sha256_HMAC.init(secretKey);
    return Base64.encodeBase64URLSafeString(sha256_HMAC.doFinal(message.getBytes()));
 }
}
