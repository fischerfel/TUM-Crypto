    KeyStore ts = KeyStore.getInstance("JKS");
    ts.load(new FileInputStream(new File("Your_New_Trust_Store_Path")), "password".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ts);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, tmf.getTrustManagers(), null);

    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

    // Open Connection .... etc. ....
