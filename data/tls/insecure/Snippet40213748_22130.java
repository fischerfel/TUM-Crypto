private KeyStore readKeyStore() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

    String password = "testPass";

    InputStream is = null;

    try {
        is = activity.getApplicationContext().getResources().openRawResource(R.raw.server_key);
        ks.load(is, password.toCharArray());
    } finally {
        if (is != null)
            is.close();
    }

    return ks;
}

private OkHttpClient getOkHttpClient() throws CertificateException, IOException, KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {

    SSLContext sslContext = SSLContext.getInstance("SSL");
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

    trustManagerFactory.init(readKeyStore());
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(readKeyStore(), "testPass".toCharArray());

    sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

    return new OkHttpClient().setSslSocketFactory(sslContext.getSocketFactory());
}
