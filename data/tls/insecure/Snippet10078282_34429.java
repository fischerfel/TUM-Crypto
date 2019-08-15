private SSLContext createSSLContext(final Context cont){
    SSLContext ssl_cont = null;
    try {
        Log.d(TAG, "TrustStore - Initializing");   
        KeyStore trustStore = KeyStore.getInstance("BKS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        InputStream trustStoreStream = cont.getResources().openRawResource(R.raw.clienttruststore);
        Log.d(TAG,"here...");
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
        Log.d(TAG, "ERROR: " + e.getMessage());
    }
    return ssl_cont;
}

OnClickListener onConnClick = new OnClickListener() {

    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        try {
            // Setup the SSL context to use the truststore and keystore
            Log.d(TAG, "Started..");
            SSLContext ssl_context = createSSLContext(cont);                
            SSLSocketFactory socketFactory = (SSLSocketFactory) ssl_context.getSocketFactory();
            socket = (SSLSocket) socketFactory.createSocket(ipadd.getText().toString().trim(), Integer.parseInt(port.getText().toString().trim()));
            dataOut = new DataOutputStream(socket.getOutputStream());
            dataIn = new DataInputStream(socket.getInputStream());
            msgin.setText("Connected");
            Log.d(TAG, "Completed..");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            msgin.setText("Not connected");
            Log.d(TAG, "ERROR: " + e.getMessage());
        }
    }
};
