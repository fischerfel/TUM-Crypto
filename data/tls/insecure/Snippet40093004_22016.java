    public static void buildClient(){
        //Misc code here.... not showing for security reasons.
        OkHttpClient client = RetrofitClient.configureClient(new OkHttpClient());
        //I make calls here to update interceptors, timeouts, etc.
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new Gson()))
            .client(client)
            .build();
    }

    //Setup the ssl stuff here
    public static OkHttpClient configureClient(final OkHttpClient client) {
        final TrustManager[] certs = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType)
                    throws CertificateException {
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType)
                    throws CertificateException {
            }
        }};

        SSLContext ssl = null;
        try {
            ssl = SSLContext.getInstance("TLS");
            ssl.init(null, certs, new SecureRandom());
        } catch (final java.security.GeneralSecurityException ex) {
        }

        try {
            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(final String hostname,
                                      final SSLSession session) {
                    return true;
                }
            };
            client.setHostnameVerifier(hostnameVerifier);
            client.setSslSocketFactory(ssl.getSocketFactory());
        } catch (final Exception e) {
        }

        return client;
    }
