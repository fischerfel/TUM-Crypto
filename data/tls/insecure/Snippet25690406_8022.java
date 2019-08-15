    SSLContext sslContext = SSLContext.getInstance("TLS");
    char[] keystorePassword = "password".toCharArray();
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(new FileInputStream("filename.jks"), keystorePassword);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, keystorePassword);
    sslContext.init(kmf.getKeyManagers(), null, null);
    HttpsConfigurator configurator = new HttpsConfigurator(sslContext);
