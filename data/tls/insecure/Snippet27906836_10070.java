protected SSLSocket getConnection(String ip, int port) throws IOException {
    try {
        KeyStore trustStore = KeyStore.getInstance("BKS");
        InputStream trustStoreStream = context.getResources().openRawResource(R.raw.server);
        trustStore.load(trustStoreStream, "myPassword".toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        SSLSocketFactory factory = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket(ip, port);
        socket.setEnabledCipherSuites(SSLUtils.getCipherSuitesWhiteList(socket.getEnabledCipherSuites()));
        return socket;
    } catch (GeneralSecurityException e) {
        throw new IOException(e.getMessage());
    }
}
