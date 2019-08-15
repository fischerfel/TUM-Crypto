public SSLContext getSSLContext1() throws Exception {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    // initialise the keystore
    char[] password = "password".toCharArray();
    KeyStore ks = KeyStore.getInstance("JKS");
    FileInputStream fis = new FileInputStream("{PARENT_PATH}\\testkey.jks");
    ks.load(fis, password);

    // setup the key manager factory
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, password);

    // setup the trust manager factory
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ks);

    // setup the HTTPS context and parameters
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    return SSLContext.getDefault();
}
