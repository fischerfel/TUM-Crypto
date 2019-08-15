private void initSslSocketFactory() {

    try {
        /*
         * SETUP TRUSTSTORE 
         */  
        KeyStore trustStore = KeyStore.getInstance("BKS");  
        TrustManagerFactory trustManagerFactory = TrustManagerFactory  
            .getInstance(TrustManagerFactory.getDefaultAlgorithm());  
        InputStream trustStoreStream = context.getResources()  
            .openRawResource(R.raw.ca);  
        trustStore.load(trustStoreStream, "000000".toCharArray());  
        trustManagerFactory.init(trustStore);  

        /* 
         * SETUP KEYSTORE 
         */  
        KeyStore keyStore = KeyStore.getInstance("PKCS12");  
        KeyManagerFactory keyManagerFactory = KeyManagerFactory  
            .getInstance(KeyManagerFactory.getDefaultAlgorithm());  
        InputStream keyStoreStream = context.getResources()  
            .openRawResource(R.raw.client);  
        keyStore.load(keyStoreStream, "000000".toCharArray());  
        keyManagerFactory.init(keyStore, "000000".toCharArray());  

        /* 
         * SETUP the SSL context to use the truststore and keystore 
         */   
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        mSslSocketFactory = sslContext.getSocketFactory();
    } catch (NoSuchAlgorithmException e) {
        Log.e(Tag, "" + e.getMessage());
        e.printStackTrace();
    } catch (KeyManagementException e) {
        Log.e(Tag, "" + e.getMessage());
        e.printStackTrace();
    }
}

public void connect() {
    mSslSock = (SSLSocket) mSslSocketFactory.createSocket();
    mSslSock.connect(new InetSocketAddress(host, port));
    mSslSock.setSoTimeout(SOCKET_TIMEOUT);
    mSslSock.setUseClientMode(true);
    mSslSock.setEnabledCipherSuites(mSslSock.getEnabledCipherSuites());
    mSslSock.setEnabledProtocols(mSslSock.getSupportedProtocols());
    mSslSock.connect(SocketAddress);
    ......
}
