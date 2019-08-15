rfbProtocol(String h, int p, Context c) throws Exception {
    // TODO Auto-generated constructor stub
    host = h;
    port = p;
    cont = c;
    try {
        // Setup the SSL context to use the truststore and keystore
        Log.d(TAG, "Initializing SSL connection");
        SSLContext ssl_context = createSSLContext(cont);              
        SSLSocketFactory socketFactory = (SSLSocketFactory) ssl_context.getSocketFactory();
        Log.d(TAG,"Creating RFB socket");
        socket = (SSLSocket) socketFactory.createSocket(host, port);
        Log.d(TAG,"RFB Socket created");
        dataOut = socket.getOutputStream();
        dataIn = new DataInputStream(new BufferedInputStream(socket.getInputStream(), 16384));
        close = false;
        Log.d(TAG,"SSL connection done");
    } catch (Exception e) {
        // TODO Auto-generated catch block
        throw(e);
    }
}

private SSLContext createSSLContext(final Context cont) throws Exception{
    SSLContext ssl_cont = null;
    try {
        // Setup truststore
        Log.d(TAG, "TrustStore - Initializing");   
        KeyStore trustStore = KeyStore.getInstance("BKS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        InputStream trustStoreStream = cont.getResources().openRawResource(R.raw.clienttruststore);
        trustStore.load(trustStoreStream, "client".toCharArray());
        trustManagerFactory.init(trustStore);
        Log.d(TAG, "TrustStore - Initialized");

        // Setup keystore
        Log.d(TAG, "KeyStore - Initializing");
        KeyStore keyStore = KeyStore.getInstance("BKS");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        InputStream keyStoreStream = cont.getResources().openRawResource(R.raw.client);
        keyStore.load(keyStoreStream, "client".toCharArray());
        keyManagerFactory.init(keyStore, "client".toCharArray());
        Log.d(TAG, "KeyStore - Initialized");

        ssl_cont = SSLContext.getInstance("TLS");
        ssl_cont.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null); 
    } catch (Exception e) {
        // TODO Auto-generated catch block
        throw(e);
    }
    return ssl_cont;
}
