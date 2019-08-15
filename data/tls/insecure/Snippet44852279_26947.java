public class SslOkHttpClient {

private static final String LOGGER = SslOkHttpClient.class.getName();
private static String alias;
private static Key privateKey;
private static X509Certificate[] certificates;
private static String KEYSTORE_PASS = "password";



public static OkHttpClient getOkHttpClient(Context context) throws GeneralSecurityException, IOException, IllegalStateException {
    if (alias == null)
        alias = HttpsUtils.getAlias(context);
    if (privateKey == null)
        privateKey = HttpsUtils.getPrivateKey(context, alias);
    if (certificates == null)
        certificates = HttpsUtils.getCertificateChain(context, alias);

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .build();

    KeyStore ks = getKeystore();

    OkHttpClient client = new OkHttpClient.Builder()
            .sslSocketFactory(getSslSocketFactory(ks), (X509TrustManager) getTrustManager(ks)[0])
            .connectionSpecs(Collections.singletonList(spec))
            .addInterceptor(interceptor)
            .build();
    return client;
}

private static SSLSocketFactory getSslSocketFactory(KeyStore keystore) throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, KeyManagementException, IllegalStateException, NoSuchProviderException {
    SSLContext sslContext;

    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, getTrustManager(keystore), null);
    return sslContext.getSocketFactory();
}

private static TrustManager[] getTrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, IllegalStateException, NoSuchProviderException {
    TrustManager[] trustManagers;
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(keystore);

    trustManagers = trustManagerFactory.getTrustManagers();

    if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
        throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
    }

    return trustManagers;
}

private static KeyStore getKeystore() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException {
    KeyStore keystore = KeyStore.getInstance("PKCS12");
    keystore.load(null, null);
    if (alias != null) {
        keystore.setKeyEntry(alias, privateKey, KEYSTORE_PASS.toCharArray(), certificates);
    }

    return keystore;
}
}
