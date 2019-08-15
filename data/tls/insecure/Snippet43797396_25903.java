OkHttpClient client = new OkHttpClient();
client.setSslSocketFactory(getPinnedCertSslSocketFactory(context));


private SSLSocketFactory getPinnedCertSslSocketFactory(Context context) {
    try {
        KeyStore trusted = KeyStore.getInstance("BKS");
        InputStream incontext.getResources().openRawResource(R.raw.prod_keystore);
        trusted.load(in, "venkat@123".toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trusted);
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        return sslContext.getSocketFactory();
    } catch (Exception e) {
        Log.e("MyApp", e.getMessage(), e);
    }
    return null;
}
