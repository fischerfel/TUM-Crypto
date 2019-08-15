    KeyStore keyStore = KeyStore.getInstance("JKS");

    // load default jvm keystore
    keyStore.load(new FileInputStream(
            System.getProperties()
                  .getProperty("java.home") + File.separator
                + "lib" + File.separator + "security" + File.separator
                + "cacerts"), "changeit".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm());

    tmf.init(keyStore);

    SSLContext ctx = SSLContext.getInstance("TLS");

    ctx.init(null, tmf.getTrustManagers(), new SecureRandom());
