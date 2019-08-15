public class ServiceGenerator {

//main donate server
private static final String SERVICE_BASE_URL = "127.0.0.1"; //add your server ip here
private static Retrofit retrofit;
private static OkHttpClient.Builder httpClient ;
private static ServiceGenerator instance;
private static Retrofit.Builder builder =
        new Retrofit.Builder()
                .baseUrl(SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

private ServiceGenerator() {
    init();
}

public static ServiceGenerator getInstance() {

    if (instance == null) {
        instance = new ServiceGenerator();
    }
    return instance;
}

public static <S> S createService(Class<S> serviceClass) {
    return retrofit.create(serviceClass);
}

public static void init(){
    httpClient =new OkHttpClient.Builder()
            .sslSocketFactory(getSSLConfig(KarizApplication.appContext).getSocketFactory());

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(Config.HTTP_LOG_LEVEL);

    httpClient.addInterceptor(logging);
    httpClient.connectTimeout(50, TimeUnit.SECONDS);
    httpClient.readTimeout(50, TimeUnit.SECONDS);
    OkHttpClient client = httpClient.build();
    retrofit = builder.client(client).build();
}
    public static Retrofit retrofit() {
    return retrofit;
}

private static SSLContext getSSLConfig(Context context) {
    try {
        // Loading CAs from an InputStream
        CertificateFactory cf = null;
        cf = CertificateFactory.getInstance("X.509");

        Certificate ca;
        // I'm using Java7. If you used Java6 close it manually with finally.
        InputStream cert = context.getResources().openRawResource(R.raw.donate_apache_selfsigned);
        ca = cf.generateCertificate(cert);

        // Creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext;
    }
    catch (Exception e){
        throw new RuntimeException(e);
    }
}

}
