public class JerseyClient {
    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "https://localhost:9028/testsecurity2/resources";

    public JerseyClient() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig(); // SSL configuration
        // SSL configuration
        config.getProperties().put(com.sun.jersey.client.urlconnection.HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new com.sun.jersey.client.urlconnection.HTTPSProperties(getHostnameVerifier(), getSSLContext()));
        client = Client.create(config);
        webResource = client.resource(BASE_URI).path("manufacturers");
    }

    public <T> T get_XML(Class<T> responseType) throws UniformInterfaceException {
        return webResource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T get_JSON(Class<T> responseType) throws UniformInterfaceException {
        return webResource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.destroy();
    }

    public void setUsernamePassword(String username, String password) {
        client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(username, password));
    }

    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                return true;
            }
        };
    }

    private SSLContext getSSLContext() {
        javax.net.ssl.TrustManager x509 = new javax.net.ssl.X509TrustManager() {

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
                return;
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
                return;
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, new javax.net.ssl.TrustManager[]{x509}, null);
        } catch (java.security.GeneralSecurityException ex) {
        }
        return ctx;
    }

}
