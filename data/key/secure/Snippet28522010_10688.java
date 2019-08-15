    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(
            "https://test.api.kolibree.com/v1/accounts/request_token/");
    post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    post.addHeader("client-id", "5");
    post.addHeader("client signature",
            hash_client_secret("https://test.api.kolibree.com/"));

    try {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("email",
                "android.test@kolibree.com"));
        nameValuePairs.add(new BasicNameValuePair("password", "test"));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(
                response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

}

/** function is used to has the client signature header **/
private static String hash_client_secret(String client_secret) {
    String hash = null;
    try {
        String secret = client_secret;
        String message = "https://test.api.kolibree.com/";
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(),
                "HmacSHA256");
        sha256_HMAC.init(secret_key);
        hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message
                .getBytes()));
    } catch (Exception e) {
        System.out.println("Error");
    }
    return hash;
}
