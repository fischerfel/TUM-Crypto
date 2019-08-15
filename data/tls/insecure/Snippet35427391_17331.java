OkHttpClient.Builder builder = new  OkHttpClient.Builder();
                KeyStore keyStore = readKeyStore();
                SSLContext sslContext = SSLContext.getInstance("SSL");
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, "password".toCharArray());
                sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
                builder.sslSocketFactory(sslContext.getSocketFactory());
                OkHttpClient client = builder.build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://192.168.5.91:9443")
                        .addConverterFactory(JacksonConverterFactory.create())
                        .client(client)
                        .build();
