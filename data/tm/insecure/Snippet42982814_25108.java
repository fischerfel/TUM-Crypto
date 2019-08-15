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

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.interceptors().add(httpLoggingInterceptor);
        client.readTimeout(180, TimeUnit.SECONDS);
        client.connectTimeout(180, TimeUnit.SECONDS);

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
       keyStore.load(null, null);

        SSLContext sslContext = SSLContext.getInstance("TLS");

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
       trustManagerFactory.init(keyStore);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, "keystore_pass".toCharArray());
        sslContext.init(null, trustAllCerts, new SecureRandom());
        client.sslSocketFactory(sslContext.getSocketFactory())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
               });

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Common.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build();

        serviceApi = retrofit.create(Api.class);
