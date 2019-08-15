    private void installSSLContextP12() throws Exception {
    KeyStore tks = KeyStore.getInstance(KeyStore.getDefaultType());
    tks.load(new FileInputStream("/home/dan/Dokumente/Zertifikate/store"), "xxx".toCharArray());                   // load truststore

    KeyStore iks = KeyStore.getInstance("PKCS12");
    iks.load(new FileInputStream("/home/dan/Dokumente/Zertifikate/danmocz_zert.p12"), "yyy".toCharArray());     // load private keystore

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());            // init truststore
    tmf.init(tks);

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(iks, "yyy".toCharArray());                                                                                    // load priv. key's pw
    KeyManager[] kms = kmf.getKeyManagers();


    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);                                                          // trust/keystore
    SSLContext.setDefault(ctx);  //That is enough to authenticate at the server
}
