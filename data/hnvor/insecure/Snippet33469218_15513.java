    String serverAddress = "https://auth.timeface.cn/aliyun/sts";
    OkHttpClient httpClient = new OkHttpClient();

    if (serverAddress.contains("https")) {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_0)
                .cipherSuites(CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA)
                .supportsTlsExtensions(true)
                .build();

        httpClient.setConnectionSpecs(Collections.singletonList(spec));
        httpClient.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        httpClient.setConnectTimeout(1, TimeUnit.HOURS);
    }

    Request request = new Request.Builder()
            .url(serverAddress)
            .get()
            .build();

    Response response = httpClient.newCall(request).execute();
    String responseStr = response.body().string();
