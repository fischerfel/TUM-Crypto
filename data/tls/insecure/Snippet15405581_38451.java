private SSLSocket getSocketConnection() throws SSLConnectionException {
    try {

        /* Load properties */
        String keystore = properties.getProperty("controller.keystore");
        String passphrase = properties.getProperty("controller.passphrase");
        String host = properties.getProperty("controller.host");
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
        SSLSocketFactory factory = ctx.getSocketFactory();

        return (SSLSocket) factory.createSocket(host, port);
    } catch (Exception e) {
        throw new SSLConnectionException(
                "Problem connecting with remote controller: "
                        + e.getMessage(), e.getCause());
    }
}
