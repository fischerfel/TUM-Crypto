public class ApiClient {

public static final String DOMAIN = "qwerty.xyz"; //not the real url obviously
public static final String BASE_URL = "https://api." + DOMAIN;

private static Retrofit retrofit = null;

private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

private static Gson gson = new GsonBuilder()
        .create();

private static Retrofit.Builder builder =
        new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson));

public static <S> S createService(Class<S> serviceClass) {
    return createService(serviceClass, null, null);
}

public static <S> S createService(Class<S> serviceClass, String username, String password) {
    Retrofit retrofit = builder.client(getUnsafeOkHttpClient())
            .build();
    return retrofit.create(serviceClass);
}

public static OkHttpClient getUnsafeOkHttpClient() {

    try {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        } };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


        OkHttpClient okHttpClient  = httpClient.sslSocketFactory(sslSocketFactory).hostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                                                            return true;
                                                    }
                                                })
                                        .build();

        return okHttpClient;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }

}
