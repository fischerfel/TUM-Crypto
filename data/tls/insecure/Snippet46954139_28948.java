       static void pinCertificate(Context context, OkHttpClient.Builder builder) {
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream cert = context.getResources().openRawResource(R.raw.certificate);
                Certificate ca;
                ca = cf.generateCertificate(cert);

                // creating a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);


                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);


                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, tmf.getTrustManagers(), null);
                builder.sslSocketFactory(sslContext.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
