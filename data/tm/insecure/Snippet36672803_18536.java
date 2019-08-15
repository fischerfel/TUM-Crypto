private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[]{
    new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers(){
            return null;
        }
        public void checkClientTrusted( X509Certificate[] certs, String authType ){}
        public void checkServerTrusted( X509Certificate[] certs, String authType ){}
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }
    }
};

public static void main(String[] args) {
    TrustStrategy acceptingTrustStrategy = 

    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
            .loadTrustMaterial(null, acceptingTrustStrategy)
            .build();

    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

    CloseableHttpClient httpClient = HttpClients.custom()
            .setSSLSocketFactory(csf)
            .build();

    HttpComponentsClientHttpRequestFactory requestFactory =
            new HttpComponentsClientHttpRequestFactory();

    requestFactory.setHttpClient(httpClient);

    RestTemplate restTemplate = new RestTemplate(requestFactory);
    String url = "https://api.stubhubsandbox.com/search/catalog/events/v3";
    RestTemplate rest = new RestTemplate();
    Map<String, String> mvm = new HashMap<String, String>();
    mvm.put("Authorization", "Bearer TOKEEEEEEEN");
    Object object = rest.postForObject(url, null, Object.class, mvm);
    System.err.println("done");


}
