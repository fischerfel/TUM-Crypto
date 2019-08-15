public static SSLSocketFactory getSSLSocketFactory(String keyStoreFileName, String password) throws Exception {
    SSLSocketFactory sslFactory = null;
    try {
        KeyStore keyStore = getKeyStore(keyStoreFileName, password);

        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keyStore);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("PKIX");//PKIX or SunX509
        keyManagerFactory.init(keyStore, password.toCharArray());

        TrustManager[] trustmanagers = factory.getTrustManagers();
        if (trustmanagers.length == 0) {
            throw new NoSuchAlgorithmException("no trust manager found");
        }

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(keyManagerFactory.getKeyManagers(), factory.getTrustManagers(), new SecureRandom());
        sslFactory = ctx.getSocketFactory();
    } catch(Exception e) {
        System.out.println("Exception while trying to get the SSLSocketFactory");
        System.out.println(e.getMessage());
    }
    return sslFactory;
}
