protected Socket ServerConnection(Context context) throws IOException{
    try {
        KeyStore trustStore = KeyStore.getInstance("BKS");
        InputStream trustStoreStream = context.getResources().openRawResource(R.raw.simplechat);
        trustStore.load(trustStoreStream, "simplechat".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(trustStore, "firstKey".toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
        trustManagerFactory.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        SSLSocketFactory factory = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket("localhost", 8000);
        socket.startHandshake();
        return socket;

    } catch (GeneralSecurityException e) {
        Log.e(this.getClass().toString(), "Exception while creating context: ", e);
        throw new IOException("Could not connect to SSL Server", e);
    }
}
