public MQQueueManager mqConnect(String Qmanager, String hostname, String channel, int port) throws Exception { 
    Hashtable<String, SSLSocketFactory> props = null;
    try {
        props = setSSLKeystore("dev-cert.jks", "Password");

        MQEnvironment.hostname = hostname;
        MQEnvironment.channel = channel;
        MQEnvironment.port = port;

        if(channel.equals(SECURE_CHANNEL)){
             MQEnvironment.sslCipherSuite="TLS_RSA_WITH_AES_128_CBC_SHA";
        }else{
             MQEnvironment.sslCipherSuite="";
        }

        MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);

        MQQueueManager mqQueueManager = (props != null) ? new MQQueueManager(Qmanager, props) : new MQQueueManager(Qmanager);
        return mqQueueManager;
    }catch (MQException mqExp) {
        mqExp.printStackTrace();
        return null;
    }
}

private Hashtable<String, SSLSocketFactory> setSSLKeystore(String fileName, String password){
    Hashtable<String, SSLSocketFactory> props = new Hashtable<String, SSLSocketFactory>();
    try{
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(fileName), password.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password.toCharArray());

        KeyStore ts = KeyStore.getInstance("JKS");
        ts.load(new FileInputStream(fileName), password.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ts);

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        props.put(MQC.SSL_SOCKET_FACTORY_PROPERTY, sslContext.getSocketFactory());
        return props;
    }catch(Exception e){
        e.printStackTrace();return null;
    }
}
