private Cluster getCluster(final String trustStoreLocation, final String trustStorePassword, final String keyStoreLoc, final String keyStorePass,final String host) throws UnrecoverableKeyException {
        final Cluster cluster;
        SSLContext sslcontext = null;

        try {
            final InputStream trustStoreStream = ClientToNode.class.getResourceAsStream(trustStoreLocation);
            final KeyStore keystore = KeyStore.getInstance("jks");
            final char[] trustChars = trustStorePassword.toCharArray();
            keystore.load(trustStoreStream, trustChars);

            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);
            final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            final InputStream keyStoreStream = ClientToNode.class.getResourceAsStream(keyStoreLoc);
            final KeyStore keyStore = KeyStore.getInstance("jks");
            final char[] pwdKeyStore = keyStorePass.toCharArray();
            keyStore.load(keyStoreStream, pwdKeyStore);

            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePass.toCharArray());
            final KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();


            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(keyManagers, trustManagers, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //String[] ciphers = {"TLS_DHE_RSA_WITH_AES_128_GCM_SHA256"};
        JdkSSLOptions sslOptions = JdkSSLOptions.builder()
                                                .withSSLContext(sslcontext)
                                                //.withCipherSuites(ciphers)
                                                .build();

        cluster = Cluster.builder()
                         .addContactPoint(host)
                         .withSSL(sslOptions)
                         .build();
        return cluster;
    }