@RequestScoped
public class WebserviceWrapper {

private Port createAndPingWebservice() {
    ....
    SSLContext sslCtx = SSL_CONTEXT_WITHOUT_CLIENT_CERTS;
    ctxt.put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory", sslCtx.getSocketFactory());
    ctxt.put("com.sun.xml.ws.transport.https.client.SSLSocketFactory", sslCtx.getSocketFactory());

    return port;
}

private static final SSLContext SSL_CONTEXT_WITHOUT_CLIENT_CERTS;

static {
    SSLContext sslCtx = null;
    try {
        sslCtx = SSLContext.getInstance("SSL");
        TrustManager[] trustManagers = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                }
            }
        };

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore ks = KeyStore.getInstance("JKS");

        try (InputStream keyStoreIn = WebserviceWrapper.class.getResourceAsStream("emptyKeystore.jks")) {
            ks.load(keyStoreIn, "changeit".toCharArray());
        }
        kmf.init(ks, "changeit".toCharArray());

        sslCtx.init(kmf.getKeyManagers(), trustManagers, null);

    } catch (NoSuchAlgorithmException | KeyStoreException | IOException | CertificateException | UnrecoverableKeyException | KeyManagementException e) {
        Logger.getLogger(WebserviceWrapper.class.getName()).log(Level.SEVERE, null, e);
    }
    SSL_CONTEXT_WITHOUT_CLIENT_CERTS = sslCtx;
}

}
