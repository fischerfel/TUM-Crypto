    InputStream caInput = null;
    try {
        caInput = new BufferedInputStream(getAssets().open("newCert.cert"));
    } catch (IOException e) {
        e.printStackTrace();
    }


    CertificateFactory certificateFactory = null;
    certificateFactory = CertificateFactory.getInstance("X.509");

    X509Certificate cert = null;
    cert = (X509Certificate) certificateFactory.generateCertificate(caInput);

    String alias = cert.getSubjectX500Principal().getName();


    KeyStore trustStore = null;
    trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

    trustStore.load(null);

    trustStore.setCertificateEntry(alias, cert);

    KeyManagerFactory kmf = null;
    kmf = KeyManagerFactory.getInstance("X509");
    String password = "abcd123";

    kmf.init(trustStore, password.toCharArray());

    KeyManager[] keyManagers = kmf.getKeyManagers();


    TrustManagerFactory tmf = null;
    tmf = TrustManagerFactory.getInstance("X509");

    tmf.init(trustStore);
    TrustManager[] trustManagers = tmf.getTrustManagers();


    SSLContext sslContext = null;
    sslContext = SSLContext.getInstance("TLS");

    sslContext.init(keyManagers, trustManagers, null);

    mServer = new MySocketServer(new InetSocketAddress("127.0.0.1", 6663));
    mServer.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));

    mServer.start();
}
