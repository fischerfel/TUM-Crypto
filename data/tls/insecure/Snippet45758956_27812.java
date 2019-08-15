BZOrderAsynchOut service = new BZOrderAsynchOut();
BZOrderAsynchOut port = service.getHTTPSPort();

// Use the BindingProvider's context to set the endpoint
BindingProvider bp = (BindingProvider)port;
    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, usuario);
bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, senha);
bp.getRequestContext().put("com.sun.xml.ws.transport.https.client.SSLSocketFactory", getTrustingSSLSocketFactory());
bp.getRequestContext().put("com.sun.xml.ws.transport.https.client.hostname.verifier", new NaiveHostnameVerifier());
port.bzOrderAsynchOut(object);

public static SSLSocketFactory getTrustingSSLSocketFactory() {
        return SSLSocketFactoryHolder.INSTANCE;
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        TrustManager[] trustManagers = new TrustManager[]{
            new NaiveTrustManager()
        };
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(new KeyManager[0], trustManagers, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory()); 
            return sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            return null;
        }
    }

    private static interface SSLSocketFactoryHolder {

        public static final SSLSocketFactory INSTANCE = createSSLSocketFactory();
    }

    private static class NaiveHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostName, SSLSession session) {
            return true;
        }
    }

    private static class NaiveTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }
