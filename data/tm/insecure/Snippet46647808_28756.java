String URL="https://google.com";

public void setUrl (String URL) {
    Log.d("Url in net-module",URL);
    this.URL = URL;
    Log.d("Url in net-module",this.URL);

}

@Provides
@Singleton
public String provideUrl() {
    Log.d("ProvideUrl","is Called");
    Log.d("url",URL);
    return URL;
}

@Provides
@Singleton
public Retrofit provideRetrofit(String URL) {
    Log.d("ProvideRetrofit","is called");
    Log.d("URL In NetModule",URL);
    Retrofit retrofit = new Retrofit.Builder()
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL)
            .build();
    return retrofit;
}

@Provides
@Singleton
public NetModule provideNetModule() {
    return new NetModule();
}

private OkHttpClient getUnsafeOkHttpClient() {
    try {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(sslSocketFactory);
        builder.addInterceptor(logging);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}}
