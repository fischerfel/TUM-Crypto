public static void doRequest() {
    ClientConfig config = new ClientConfig();
    RestClient restClient = new RestClient(config);
    Resource resource = restClient.resource(serviceURL);
    trustAllCertificates();//trust all certificates before doing the request 
    ClientResponse clientResponse = resource.get(); 
}
public static void trustAllCertificates() throws RuntimeException {
    try {
        TrustManager[] trustManager = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override
                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
                @Override
                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        SSLContext sc = SSLContext.getInstance("TLS");//or SSL, it depends on the certificate
        sc.init(null, trustManager, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e.getMessage());
    } catch (KeyManagementException e) {
        throw new RuntimeException(e.getMessage());
    } 
}
