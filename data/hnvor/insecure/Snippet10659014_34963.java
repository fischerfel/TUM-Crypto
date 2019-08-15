private static String UTF8 = "UTF-8";
public void sendMessage(String text, String registrationId) {
    try {
        StringBuilder postDataBuilder = new StringBuilder();
        postDataBuilder
            .append("registration_id")
            .append("=")
            .append(registrationId);

        postDataBuilder.append("&")
            .append("collapse_key")
            .append("=")
            .append("0");

        postDataBuilder.append("&")
            .append("data.payload")
            .append("=")
            .append(URLEncoder.encode(text, UTF8));

        byte[] postData = postDataBuilder.toString().getBytes(UTF8);

        URL url = new URL("https://android.apis.google.com/c2dm/send");

        HostnameVerifier hVerifier = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(hVerifier);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty(
            "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty(
            "Content-Length", Integer.toString(postData.length));
        conn.setRequestProperty(
            "Authorization", "Bearer " + credential.getAccessToken());

        OutputStream out = conn.getOutputStream();
        out.write(postData);
        out.close();
        int sw = conn.getResponseCode();
        System.out.println("" + sw);
    }
    catch (IOException ex){
        //handle this
    }
}
