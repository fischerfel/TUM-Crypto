private static ServerSocketFactory getServerSocketFactory(String type) {
    if (type.equals("TLS")) {
        SSLServerSocketFactory ssf = null;

        Properties systemProps = System.getProperties();
        systemProps.put( "javax.net.ssl.trustStore", "cacerts.jks");
        systemProps.put( "javax.net.ssl.trustStorePassword", "p@ssw0rd");
        System.setProperties(systemProps);

        try {
            // set up key manager to do server authentication
            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks;
            char[] passphrase = "p@ssw0rd".toCharArray();

            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");
            ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("keystore.jks"), passphrase);
            kmf.init(ks, passphrase);
            ctx.init(kmf.getKeyManagers(), null, null);

            ssf = ctx.getServerSocketFactory();
            return ssf;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
