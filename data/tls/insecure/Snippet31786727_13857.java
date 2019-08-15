public static String httpsPost(String url, String body, String mediaType, String encoding) {
   disableCertificateValidation();
   HttpClient client = new HttpClient();
   StringRequestEntity requestEntity = new StringRequestEntity(body, mediaType, encoding);
   PostMethod method = new PostMethod(url);
   method.setRequestEntity(requestEntity);
   client.executeMethod(method);
}

public static void disableCertificateValidation() {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }};

    // Ignore differences between given hostname and certificate hostname
    HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) { return true; }
    };

    // Install the all-trusting trust manager
    try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    } catch (Exception e) {}
}
