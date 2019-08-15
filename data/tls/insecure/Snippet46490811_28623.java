    HttpUrl.Builder urlBuilder = HttpUrl.parse(url_link).newBuilder();
    urlBuilder.addQueryParameter("lang", "ru");
    urlBuilder.addQueryParameter("location", ";");
    urlBuilder.addQueryParameter("O", "A");
    urlBuilder.addQueryParameter("appid", "com.aero.merapp");
    urlBuilder.addQueryParameter("userId", "295");
    urlBuilder.addQueryParameter("id_operator", "295");
    urlBuilder.addQueryParameter("token", "3hqIuH2vAofaQw5N1GGW-WIhcssnEz1Ze1yICg_aPA91bG9KYtF14oku4IH07JIHOwu0ZHMcQ");
    urlBuilder.addQueryParameter("cop", "190");
    urlBuilder.addQueryParameter("x", "");
    String url = urlBuilder.build().toString();

    Request request = new Request.Builder()
            .url(url)
            .build();

    Response response = client.newCall(request).execute();

    Log.e("RESULT: ", response.body().string());

    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            } else {
                Log.e("RESULT2: ", response.message());
            }
        }


    });
}

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
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
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
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
    }
}
