private SSLServerSocketFactory getSSLServerSocketFactory() {    
    SSLContext ctx = null;
    try {
        KeyManagerFactory kmf;
        TrustManagerFactory tmf;
        KeyStore ks;
        char[] password = "password".toCharArray();
        // Returns a SSLContext object that implements the specified secure
        // socket protocol.
        ctx = SSLContext.getInstance("TLS");
        kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());
        tmf = TrustManagerFactory.getInstance(TrustManagerFactory
                .getDefaultAlgorithm());
        // returns keystore object of type Java KeyStore
        ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keystore), password);
        kmf.init(ks, password);
        tmf.init(ks);
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    } catch (Exception e) {
        e.printStackTrace();
    }

    return ctx.getServerSocketFactory();
}
