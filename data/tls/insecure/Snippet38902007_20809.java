  Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(baseURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(getOkHttpClient(context, new OkHttpClient(), context.getResources().openRawResource(R.raw.mysslcertificate)))
                            .build();

    retrofit.create(apiClass);


    public static OkHttpClient getOkHttpClient(Context context,OkHttpClient client, InputStream inputStream) {

        try {
            if (inputStream != null) {
                SSLContext sslContext = sslContextForTrustedCertificates(inputStream);


                if (sslContext != null) {
                   client = client.newBuilder()
                                        .sslSocketFactory(sslContext.getSocketFactory()).build();


                    else {

                        CLog.i(Constants.LOG_TAG_HTTPLIBRARY,"GZip not done because it is not a Analytics data");

                        client = client.newBuilder()
                                .sslSocketFactory(sslContext.getSocketFactory()).build();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return client;
    }


     private static SSLContext sslContextForTrustedCertificates(InputStream in) {
        try {
            CertificateFactory e = CertificateFactory.getInstance("X.509");
            Collection certificates = e.generateCertificates(in);
            if (certificates.isEmpty()) {
                throw new IllegalArgumentException("expected non-empty set of trusted certificates");
            } else {
                char[] password = "password".toCharArray();
                KeyStore keyStore = newEmptyKeyStore(password);
                int index = 0;
                Iterator keyManagerFactory = certificates.iterator();
                while (keyManagerFactory.hasNext()) {
                    Certificate trustManagerFactory = (Certificate) keyManagerFactory.next();
                    String sslContext = Integer.toString(index++);
                    keyStore.setCertificateEntry(sslContext, trustManagerFactory);
                }



                KeyManagerFactory var10 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                var10.init(keyStore, password);
                TrustManagerFactory var11 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                var11.init(keyStore);
                SSLContext var12 = SSLContext.getInstance("TLS");
                var12.init(var10.getKeyManagers(), var11.getTrustManagers(), new SecureRandom());

                return var12;
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }
        return null;
    }
