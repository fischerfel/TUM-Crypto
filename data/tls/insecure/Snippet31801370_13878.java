try {
    String uri = String.format("https://api.parse.com/");
    URL url = new URL(uri);
    // NOTE: If you already have a binary array of bytes, then
    //       just use this in place of 'postData' below
    String request        = "Some request to send";
    byte[] postData       = request.getBytes(StandardCharsets.UTF_8);

    HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
    connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
    connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    SSLContext sslContext = SSLContext.getInstance("TLS");

    // this mock TrustManager trusts EVERYTHING, which probably is NOT what you want
    // in production.  Nevertheless, to get up and running this should be fine.
    TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
        public X509Certificate[] getAcceptedIssuers() { return null; }
    };
    sslContext.init(null, new TrustManager[]{tm}, null);
    connection.setSSLSocketFactory(sslContext.getSocketFactory());
    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
    wr.write(postData);

    InputStream xml = connection.getInputStream();
    StringWriter writer = new StringWriter();
    IOUtils.copy(xml, writer);
    String output = writer.toString();

    System.out.println("Here is the response output: " + output);
} catch (Exception e) {
    System.out.println("Error happened when calling API parse web service.");
}
