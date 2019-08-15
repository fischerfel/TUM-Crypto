public static void send() {
    try {

        String key = "key here";
        String secret = "secret here";

        // timestamp
        Date date= new java.util.Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timestamp = dateFormat.format(date);

        // other items
        String httpVerb = "GET";
        String path = "/v1/users/username/aaa/calls/data";
        String contentType = "application/json";
        String canonicalizedHeaders = "x-timestamp:" + timestamp;

        String contentMd5 =""; // because it is a GET 

        // signing
        String stringToSign = httpVerb + "\n" + contentMd5 + "\n" + contentType + "\n" + canonicalizedHeaders + "\n" + path;
        String signature = signature(secret, stringToSign);
        String authorization = "Application " + key + ":" + signature;

        // make the call
        URL url = new URL("https://reportingapi.sinch.com" + path/*+"?_start=2016-06-13&_stop=2016-07-10"*/);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("content-type", "application/json");
        connection.setRequestProperty("x-timestamp", timestamp);
        connection.setRequestProperty("authorization", authorization);

        StringBuilder response = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ( (line = br.readLine()) != null)
            response.append(line);

        br.close();

        System.out.println(url);
        System.out.println(response.toString());
    } catch (IOException e) {
        e.printStackTrace();
    }
}
private static String signature(String secret, String message) {
    String signature = "";
    try {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(Base64.decodeBase64(secret.getBytes()), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        signature = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
    } catch (Exception e){
        System.out.println("Error");
    }
    return signature;
}
