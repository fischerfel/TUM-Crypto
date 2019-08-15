try {
    KeyStore keyStore = KeyStore.getInstance("PKCS12");

    keyStore.load(getClass().getResourceAsStream("cert.p12"), "<password>".toCharArray());
    KeyManagerFactory keyMgrFactory = KeyManagerFactory.getInstance("SunX509");
    keyMgrFactory.init(keyStore, "<password>".toCharArray());

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyMgrFactory.getKeyManagers(), null, null);
    SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

    SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);
    String[] cipherSuites = sslSocket.getSupportedCipherSuites();
    sslSocket.setEnabledCipherSuites(cipherSuites);
    sslSocket.startHandshake();
}catch()...
