// Create the OkHttp client        
OkHttpClient okHttpClient = new OkHttpClient();
SSLContext sslContext;
try {
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, null, null);
} catch (GeneralSecurityException e) {
    throw new AssertionError(); // The system has no TLS. Just give up.
}
Client client = okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());

// Create the Retrofit RestAdapter
RestAdapter.LogLevel logLevel = BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE;
return new RestAdapter.Builder()
        .setEndpoint(BuildConfig.API_URL)
        .setClient(client)
        .setConverter(new GsonConverter(getGson()))
        .setRequestInterceptor(new RetrofitAuthenticationInterceptor())
        .setLogLevel(logLevel)
        .build()
        .create(ApiServiceV1.class);
