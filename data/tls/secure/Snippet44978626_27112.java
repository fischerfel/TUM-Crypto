public class SslOkHttpClient {

private static final String LOGGER = SslOkHttpClient.class.getName();
private static String alias;
private static Key privateKey;
private static X509Certificate[] certificates;
private static String KEYSTORE_PASS = "secret";

  public static OkHttpClient getOkHttpClient(Context context) throws GeneralSecurityException, IOException, IllegalStateException {
        if (alias == null)
            alias = HttpsUtils.getAlias(context);
        if (privateKey == null)
            privateKey = HttpsUtils.getPrivateKey(context, alias);
        if (certificates == null)
            certificates = HttpsUtils.getCertificateChain(context, alias);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(getSslSocketFactory(), getAllTrustManager())
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
        return client;
    }

    private static SSLSocketFactory getSslSocketFactory() throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, KeyManagementException, IllegalStateException, NoSuchProviderException, UnrecoverableKeyException {
        SSLContext sslContext;

        sslContext = SSLContext.getInstance("TLSv1.2");

        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(null, null);
        if (alias != null) {
            keystore.setKeyEntry(alias, privateKey, KEYSTORE_PASS.toCharArray(), certificates);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(keystore, KEYSTORE_PASS.toCharArray());

        sslContext.init(kmf.getKeyManagers(), null, null);
        return sslContext.getSocketFactory();
    }

    private static X509TrustManager getAllTrustManager() throws KeyStoreException, NoSuchAlgorithmException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

}
