        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        InputStream key = getClass().getResourceAsStream("apns-dev-key.p12");
        char[] c = key.toString().toCharArray();

        keyStore.load(getClass().getResourceAsStream("apns-dev-cert.p12"), c);
        KeyManagerFactory keyMgrFactory = KeyManagerFactory.getInstance("SunX509");
        keyMgrFactory.init(keyStore, c);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyMgrFactory.getKeyManagers(), null, null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);
        String[] cipherSuites = sslSocket.getSupportedCipherSuites();
        sslSocket.setEnabledCipherSuites(cipherSuites);
        sslSocket.startHandshake();
