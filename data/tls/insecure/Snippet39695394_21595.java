public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.d("TRUST", "authType: " + authType);

                if (authType == null || authType.length() == 0) {
                    Log.d("TRUST", "no authtype present");
                    throw new IllegalArgumentException("authType");
                }

                if (chain == null || chain.length == 0) {
                    Log.d("TRUST", "no certs in chain");
                    throw new IllegalArgumentException("chain");
                }

                Log.d("TRUST", "chainLength: " + chain.length);

                try {
                    chain[0].checkValidity();
                } catch (Exception e) {
                    throw new CertificateException("Certificate not valid or trusted.");
                }
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[]{tm}, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}
