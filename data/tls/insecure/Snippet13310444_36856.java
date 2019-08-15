    char[] keyStorePassword = "changeit".toCharArray();
    KeyStore ks = KeyStore.getInstance("JKS");
    //ks.load(keyStoreStream, keyStorePassword);

    char[] certificatePassword = "changeit".toCharArray();
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, certificatePassword);

    KeyManager[] keyManagers = kmf.getKeyManagers();
    javax.net.ssl.TrustManager tm = new MyTrustMgr();
    javax.net.ssl.TrustManager[] trustManagers = new javax.net.ssl.TrustManager[]{tm };
    SecureRandom secureRandom = new SecureRandom();

    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(keyManagers, trustManagers, secureRandom);
    return ctx;
