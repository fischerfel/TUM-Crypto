            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = getApplicationContext().getResources()
                    .openRawResource(R.raw.my_certificate);//new BufferedInputStream(is);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }

            /** Create a KeyStore containing our trusted CAs **/
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            /**Create a TrustManager that trusts the CAs in our KeyStore **/
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            /** Create an SSLContext that uses our TrustManager **/
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
