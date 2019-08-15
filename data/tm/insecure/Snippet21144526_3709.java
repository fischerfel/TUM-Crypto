public class SimpleHttps {
    public static final String SERVER_NAME = "https://localhost:8090";
    public static final URI BASE_URI = URI.create(SERVER_NAME);

    private static class TrustAllCerts implements X509TrustManager {
        @Override public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }
        @Override public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }
        @Override public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
    }

    private final static TrustManager[] trustAllCerts = new TrustManager[] { new TrustAllCerts() };

    public static SSLEngineConfigurator getSslEngineConfig() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        SSLEngineConfigurator sslEngineConfigurator = new SSLEngineConfigurator(sc, false, false, false);
        return sslEngineConfigurator;
    }

    public static void main(String[] args) throws Exception {
        final ResourceConfig rc = new ResourceConfig().register(MyResource.class);
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc, true, getSslEngineConfig());

        System.in.read();

        httpServer.stop();
    }
}
