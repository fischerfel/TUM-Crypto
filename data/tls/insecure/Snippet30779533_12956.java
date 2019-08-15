OkHttpClient okHttpClient = new OkHttpClient();
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream instream = context.getResources().openRawResource(R.raw.pem_certificate);
            Certificate ca;
            ca = cf.generateCertificate(instream);
            KeyStore kStore = KeyStore.getInstance(KeyStore.getDefaultType());
            kStore.load(null, null);
            kStore.setCertificateEntry("ca", ca);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(kStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (CertificateException
                | KeyStoreException
                | NoSuchAlgorithmException
                | IOException
                | KeyManagementException e) {
            e.printStackTrace();
        }

        baseURL = endpoint;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(baseURL)
                .setClient(new OkClient(okHttpClient))
                .build();

        service = restAdapter.create(DishService.class);
