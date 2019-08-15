public static void main(String[] args) throws Exception {

    DefaultHttpClient httpClient = new DefaultHttpClient();

    try {
        SSLContext ctx = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = getTrustManagers("jks", new FileInputStream(new File("cacerts")), "changeit");
        KeyManager[] keyManagers = getKeyManagers("pkcs12", new FileInputStream(new File("clientCert.pfx")), "password");
        ctx.init(keyManagers, trustManagers, new SecureRandom());
        SSLSocketFactory factory = new SSLSocketFactory(ctx, new StrictHostnameVerifier());

        ClientConnectionManager manager = httpClient.getConnectionManager();
        manager.getSchemeRegistry().register(new Scheme("https", 443, factory));

        //as before
    }
}

protected static KeyManager[] getKeyManagers(String keyStoreType, InputStream keyStoreFile, String keyStorePassword) throws Exception {
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(keyStoreFile, keyStorePassword.toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keyStore, keyStorePassword.toCharArray());
    return kmf.getKeyManagers();
}

protected static TrustManager[] getTrustManagers(String trustStoreType, InputStream trustStoreFile, String trustStorePassword) throws Exception {
    KeyStore trustStore = KeyStore.getInstance(trustStoreType);
    trustStore.load(trustStoreFile, trustStorePassword.toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);
    return tmf.getTrustManagers();
}
