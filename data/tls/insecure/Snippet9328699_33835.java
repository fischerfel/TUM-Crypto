private static SSLContext createEasySSLContext() throws IOException {
    try {
        SSLContext context = SSLContext.getInstance("TLS");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(sampleKeystore, "password".toCharArray());

        CustomX509TrustManager trustManager = new CustomX509TrustManager(null);
        context.init(keyManagerFactory.getKeyManagers(), new TrustManager[]{trustManager}, null);
        return context;
    } catch (Exception e) {
        throw new IOException(e.getMessage());
    }
}
