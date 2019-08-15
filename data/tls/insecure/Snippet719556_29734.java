public void initSSL() throws IOException, KeyStoreException, NoSuchAlgorithmException,
                             CertificateException, UnrecoverableKeyException, KeyManagementException {
    char[] passphrase = "scared".toCharArray();
    System.out.println(java.security.KeyStore.getDefaultType());
    boolean handshakedone=false;

    KeyStore keystore1 = KeyStore.getInstance("jks");
    FileInputStream fis = new FileInputStream("C:\\\\temp\\\\work.jks");
    keystore1.load(fis, passphrase);
    fis.close();
    KeyStore ksTrust = KeyStore.getInstance("jks");
    FileInputStream fis2 =  new FileInputStream("C:\\\\temp\\\\work.jks");
    ksTrust.load(fis2, passphrase);

    KeyManagerFactory kmf =    KeyManagerFactory.getInstance("SunX509");

    // KeyManager's decide which key material to use.
    kmf.init(keystore1, passphrase);

    // TrustManager's decide whether to allow connections.
    TrustManagerFactory tmf =   TrustManagerFactory.getInstance("SunX509");
    tmf.init(ksTrust);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    SSLServerSocketFactory sslserversocketfactory =(SSLServerSocketFactory) sslContext.getServerSocketFactory();
    sslserversocket =(SSLServerSocket) sslserversocketfactory.createServerSocket(2443);
    sslserversocket.setUseClientMode(false);

    //Context conte
    ServerSocketFactory serversocketfactory =(ServerSocketFactory) sslContext.getServerSocketFactory();

    //  serverSocket = new ServerSocket(2443);
    System.out.println("OK we are set up");
}
