System.setProperty("javax.net.ssl.keyStore", "herong.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "HerongJKS");

String ksName = "herong.jks";
        char ksPass[] = "HerongJKS".toCharArray();
        char ctPass[] = "My1stKey".toCharArray();
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(server.class.getResourceAsStream(ksName), ksPass);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, ctPass);
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), null, null);
            //listen for connections
            }
