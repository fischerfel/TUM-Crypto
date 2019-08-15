private SSLContext createSSLContext(final Context cont){
    SSLContext ssl_cont = null;
    try {
        Log.d(TAG, "TrustStore - Initializing");   
        KeyStore trustStore = KeyStore.getInstance("BKS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        InputStream trustStoreStream = cont.getResources().openRawResource(R.raw.myclienttruststore);
        trustStore.load(trustStoreStream, "client".toCharArray());
        trustManagerFactory.init(trustStore);
        Log.d(TAG, "TrustStore - Initialized");

        // Setup keystore
        Log.d(TAG, "KeyStore - Initializing");
        KeyStore keyStore = KeyStore.getInstance("BKS");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        InputStream keyStoreStream = cont.getResources().openRawResource(R.raw.myclient);
        keyStore.load(keyStoreStream, "client".toCharArray());
        keyManagerFactory.init(keyStore, "client".toCharArray());
        Log.d(TAG, "KeyStore - Initialized");

        ssl_cont = SSLContext.getInstance("TLS");
        ssl_cont.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null); 
    } catch (Exception e) {
        // TODO Auto-generated catch block
        alertbox("SSLClient", "ERROR: " + e.getMessage());
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
            Log.d(TAG,"here 1...");
            SSLSocketFactory socketFactory = (SSLSocketFactory) ssl_context.getSocketFactory();
            Log.d(TAG,"here 2...");
            socket = (SSLSocket) socketFactory.createSocket(ipadd.getText().toString().trim(), Integer.parseInt(port.getText().toString().trim()));
            Log.d(TAG,"here 3...");
            dataOut = new DataOutputStream(socket.getOutputStream());
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut.writeUTF("Hello !!");
            msgin.setText("Connected");
            Log.d(TAG, "Completed..");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            msgin.setText("Not connected");
            alertbox("Main", "ERROR: " + e.getMessage());
            Log.d(TAG, "ERROR: " + e.getMessage());
        }
    }
};
