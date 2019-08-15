public class RetrofitApi {
    private static PublicApi retrofit = null;
    private Context context;


    public PublicApi getClient(String url, Context context, Integer value) {
        OkHttpClient okHttpClient;
        this.context = context;
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
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            if (value == 1) {
                //For get Log see D/OkHttp
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                // set your desired log level
                logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
                File httpCacheDirectory = new File(this.context.getCacheDir(), "responses");
                int cacheSize = 10 * 1024 * 1024; // 10 MiB
                Cache cache = new Cache(httpCacheDirectory, cacheSize);

                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    okHttpClient = builder.
                            addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                            .addInterceptor(OFFLINE_INTERCEPTOR)
                            .addInterceptor(logging)
                            .cache(cache)
                            .build();
                } else {
                    okHttpClient = new OkHttpClient.Builder()
                            .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                            .addInterceptor(OFFLINE_INTERCEPTOR)
                            .addInterceptor(logging)
                            .cache(cache)
                            .build();
                }


            } else {
                okHttpClient = builder.build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                //.client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(PublicApi.class);

        return retrofit;
    }

    private Interceptor REWRITE_RESPONSE_INTERCEPTOR = chain -> {
        Response originalResponse = chain.proceed(chain.request());
        String cacheControl = originalResponse.header("Cache-Control");

        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + 60)
                    .build();
        } else {
            return originalResponse;
        }
    };


    private Interceptor OFFLINE_INTERCEPTOR = chain -> {
        Request request = chain.request();
        if (!Utils.isNetworkAvailable(context)) {
            int maxStale = 60 * 60 * 24 * 2; // tolerate 2-days stale
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }

        return chain.proceed(request);
    };
}
