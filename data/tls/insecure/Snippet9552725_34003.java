    try {

        /*
         * Set up a key manager for client authentication
         * if asked by the server.  Use the implementation's
         * default TrustStore and secureRandom routines.
         */
        Properties systemProps = System.getProperties();
        systemProps.put( "javax.net.ssl.trustStore", "cacerts.jks");
        systemProps.put( "javax.net.ssl.trustStorePassword", "changeit");
        System.setProperties(systemProps);

        SSLSocketFactory factory = null;
        try {
            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks;
            char[] passphrase = "changeit".toCharArray();

            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");
            ks = KeyStore.getInstance("JKS");

            ks.load(new FileInputStream("keystore.jks"), passphrase);

            kmf.init(ks, passphrase);
            ctx.init(kmf.getKeyManagers(), null, null);

            factory = ctx.getSocketFactory();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
