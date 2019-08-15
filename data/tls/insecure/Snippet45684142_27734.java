/* Get the JKS contents */
        final KeyStore keyStore = KeyStore.getInstance("JKS");
        try (final InputStream is = new FileInputStream(fullPathOfKeyStore())) {
            keyStore.load(is, JKS_PASSWORD);
        }
        final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());
        kmf.init(keyStore, KEY_PASSWORD);
        final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory
                .getDefaultAlgorithm());
        tmf.init(keyStore);

        /*
         * Creates a socket factory for HttpsURLConnection using JKS
         * contents
         */
        final SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());
        final SSLSocketFactory socketFactory = sc.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
