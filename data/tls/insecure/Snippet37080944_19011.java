private void doConnectToServer()
{
    SSLSocketFactory        sslSocketFactory;
    KeyManagerFactory       keyManagerFactory;
    SSLContext              sslContext;
    KeyStore                keyStore;
    char[]                  keyStorePassphrase;
    SSLSocket               sslSocket;

    try
    {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        System.setProperty("javax.net.ssl.trustStore", "samplecacerts");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        sslContext = SSLContext.getInstance("SSL");
        keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyStore = KeyStore.getInstance("JKS");

        keyStorePassphrase = "passphrase".toCharArray();
        keyStore.load(new FileInputStream("testkeys"), keyStorePassphrase);

        keyManagerFactory.init(keyStore, keyStorePassphrase);
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        sslSocketFactory = sslContext.getSocketFactory();

        sslSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();

        sslSocket = (SSLSocket)sslSocketFactory.createSocket("127.0.0.1", 12345);

        sslSocket.startHandshake(); //line that throws the exception

        cts = new CTS(sslSocket, this);
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
