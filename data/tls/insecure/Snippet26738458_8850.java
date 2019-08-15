        System.setProperty("javax.net.debug", "ssl");

        String cacerts_file = System.getProperty("java.home")
                + "/lib/security/cacerts".replace('/', File.separatorChar);

        System.out.println(System.getProperty("javax.net.ssl.trustStore"));
        System.out.println("cacerts_file: " + cacerts_file);

        System.out.println(System.getProperty("javax.net.ssl.trustStore"));

        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream in2 = new FileInputStream(cacerts_file);
        ks.load(in2, "changeit".toCharArray());

        System.out.println(ks.containsAlias("badoojira"));
        Enumeration<String> lAliases = ks.aliases();
        while (lAliases.hasMoreElements()) {
            String lAlias = (String) lAliases.nextElement();
            System.out.println(lAlias);
        }

        javax.net.ssl.KeyManagerFactory lKeyManagerFactory = javax.net.ssl.KeyManagerFactory
                .getInstance("SunX509");
        lKeyManagerFactory.init(ks, "changeit".toCharArray());
        javax.net.ssl.SSLContext lSSLContext = javax.net.ssl.SSLContext.getInstance("TLS");
        lSSLContext.init(lKeyManagerFactory.getKeyManagers(), null, null);
        SSLSocketFactory lSSLSocketFactory = lSSLContext.getSocketFactory();
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(lSSLSocketFactory);
        SSLSocket socket = (SSLSocket) lSSLSocketFactory
                .createSocket("wiki.badoojira.com", 443); 
        String[] suites = socket.getSupportedCipherSuites();
        socket.setEnabledCipherSuites(suites);

        // start handshake
        socket.startHandshake();
