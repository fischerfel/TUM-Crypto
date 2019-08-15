String request = null;
String result = "";
URL url;
String requestContentBase64String = "";
HttpURLConnection urlConnection = null;
try {
    url = new URL("http://mysapi.com");
    urlConnection = (HttpURLConnection) url.openConnection();
    urlConnection.setRequestMethod("GET");

    Date epochStart = new Date(0);

    String requestTimeStamp = "" + ((new Date().getTime() - epochStart.getTime()) / 1000);
    String nonce = java.util.UUID.randomUUID().toString().replace("-", "");

    if (request != null) {
        byte[] content = request.getBytes();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] requestContentHash = md.digest(content);
        requestContentBase64String = Base64.getUrlEncoder().encodeToString(requestContentHash);
    }
    String signatureRawData = String.format("%s%s%s%s%s%s", ApiId, urlConnection.getRequestMethod(),
            url.toString().toLowerCase(), requestTimeStamp, nonce, requestContentBase64String);

    byte[] secretKeyByteArray = ApiKey.getBytes();
    byte[] signature = signatureRawData.getBytes("UTF-8");
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(secretKeyByteArray, "HmacSHA256");
    sha256_HMAC.init(secret_key);
    byte[] signatureBytes = sha256_HMAC.doFinal(signature);
    String requestSignatureBase64String = Base64.getEncoder().encodeToString(signatureBytes);

    String header = String.format("amx %s:%s:%s:%s", ApiId, requestSignatureBase64String, nonce,
            requestTimeStamp);
    urlConnection.setRequestProperty("Authorization", header);

    InputStream in = urlConnection.getInputStream();
    InputStreamReader reader = new InputStreamReader(in);

    int data = reader.read();
    while (data != -1) {
        char current = (char) data;
        result += current;
        data = reader.read();
    }
    System.out.println(result);

} 
catch (MalformedURLException e)
{
    e.printStackTrace();
} 
catch (IOException e) 
{
    e.printStackTrace();
}
catch (NoSuchAlgorithmException e) 
{
    e.printStackTrace();
} 
catch (InvalidKeyException e) 
{
    e.printStackTrace();
}
catch (Exception e) 
{

    e.printStackTrace();
}
