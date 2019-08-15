private static String generateSasToken(URI uri) {
    String targetUri;
    try {
        targetUri = URLEncoder
        .encode(uri.toString().toLowerCase(), "UTF-8")
        .toLowerCase();

        long expiresOnDate = System.currentTimeMillis();
        int expiresInMins = 20; // 1 hour
        expiresOnDate += expiresInMins * 60 * 1000;
        long expires = expiresOnDate / 1000;
        String toSign = targetUri + "\n" + expires;

        // Get an hmac_sha1 key from the raw key bytes
        byte[] keyBytes = sasKey.getBytes("UTF-8");
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

        // Get an hmac_sha1 Mac instance and initialize with the signing key
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        // Compute the hmac on input data bytes
        byte[] rawHmac = mac.doFinal(toSign.getBytes("UTF-8"));

        // using Apache commons codec for base64
//      String signature = URLEncoder.encode(
//      Base64.encodeBase64String(rawHmac), "UTF-8");
        String rawHmacStr = new String(Base64.encodeBase64(rawHmac, false),"UTF-8");
        String signature = URLEncoder.encode(rawHmacStr, "UTF-8");

        // construct authorization string
        String token = "SharedAccessSignature sr=" + targetUri + "&sig="
        + signature + "&se=" + expires + "&skn=" + sasKeyName;
        return token;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

public static void Send(String topic, String subscription, String msgToSend) throws Exception {

        String url = uri+topic+"/messages";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // Add header
        String token = generateSasToken(new URI(uri));
        post.setHeader("Authorization", token);
        post.setHeader("Content-Type", "text/plain");
        post.setHeader(subscription, subscription);
        StringEntity input = new StringEntity(msgToSend);
        post.setEntity(input);

        System.out.println("Llamando al post");
        HttpResponse response = client.execute(post);
        System.out.println("Response Code : " 
                + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() != 201)
            throw new Exception(response.getStatusLine().getReasonPhrase());

}
