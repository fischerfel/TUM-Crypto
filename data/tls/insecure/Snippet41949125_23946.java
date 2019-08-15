System.setProperty("javax.net.ssl.keyStore", "/path/to/KeyStoreFile.jks");
System.setProperty("javax.net.ssl.keyStorePassword", "password");
System.setProperty("javax.net.ssl.trustStore", "/path/to/truststore.jks");
System.setProperty("javax.net.ssl.trustStoreType", "JKS");*/

try {
    String keyPass = "password";
    // Establish SSL handshake before the first web service method is called
    KeyStore keyStore = KeyStore.getInstance("JKS");
    keyStore.load(new FileInputStream("/path/to/KeyStoreFile.jks"),
    keyPass.toCharArray());
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, keyPass.toCharArray());

    KeyStore trustStore = KeyStore.getInstance("JKS");
    trustStore.load(new FileInputStream("/path/to/truststore.jks"),
                            null);
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStore);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
    SSLContext.setDefault(sslContext);

    //SSLSocketFactory sSLSocketFactory = sslContext.getSocketFactory();
    //SSLSocket sslSocket = (SSLSocket) sSLSocketFactory.createSocket();
    //sslSocket.setUseClientMode(true);

    //HttpsURLConnection.setDefaultSSLSocketFactory(sSLSocketFactory);
} catch (Exception e) {
    e.printStackTrace();
}
