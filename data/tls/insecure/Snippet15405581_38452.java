private SSLServerSocket getServerSocket() throws SSLConnectionException {
    try {

        /* Load properties */
        Properties properties = getProperties("controller.properties");

        String keystore = properties.getProperty("controller.keystore");
        String passphrase = properties.getProperty("controller.passphrase");
        int port = Integer.parseInt(properties
                .getProperty("controller.port"));

        /* Create keystore */
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(keystore), passphrase.toCharArray());

        /* Get factory for the given keystore */
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null, tmf.getTrustManagers(), null);
        SSLServerSocketFactory factory = ctx.getServerSocketFactory();

        return (SSLServerSocket) factory.createServerSocket(port);
    } catch (Exception e) {
        throw new SSLConnectionException(
                "Problem starting auth server: "
                        + e.getMessage(), e.getCause());
    }
}
