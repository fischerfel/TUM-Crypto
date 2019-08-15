public class JerseyTestClient {

private static final Logger LOG = Logger.getLogger(JerseyTestClient.class.getName());

public static void sendTestRequest() {

    try {

        Client client = Client.create(configureClient());
        WebResource webResource = client.resource("https://server/endpoint/");
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        List<Hardware> output = response.getEntity(new GenericType<List<Hardware>>() {});
        LOG.severe("Output from Server .... \n");
        LOG.severe("Nr of entries: " + output.size());

    } catch (Exception e) {
        LOG.log(Level.SEVERE, " test request failed", e);
    }
}

public static ClientConfig configureClient() {
    TrustManager[ ] certs = new TrustManager[ ] {
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    LOG.severe("getAcceptedIssuers");
                    return null;
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    LOG.severe("checkServerTrusted");
                }
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    LOG.severe("checkClientTrusted");
                }
            }
    };
    SSLContext ctx = null;
    try {
        ctx = SSLContext.getInstance("TLS");
        ctx.init(null, certs, null);
    } catch (java.security.GeneralSecurityException e) {
        LOG.log(Level.SEVERE, "Error", e);
    }
    HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            LOG.severe("verify");
            return true;
        }
    });

    ClientConfig config = new DefaultClientConfig();
    try {
        config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(
            new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    LOG.severe("verify");
                    return true;
                }
            }, 
            ctx
        ));
    } catch(Exception e) {
        LOG.log(Level.SEVERE, "Error", e);
    }
    return config;
}
