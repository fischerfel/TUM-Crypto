// loading CAs from an InputStream
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream cert = context.getResources().openRawResource(R.raw.servercert);
            Certificate ca;
            try {
                ca = cf.generateCertificate(cert);
            } finally {
                cert.close();
            }

            // creating a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // creating a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // creating an SSLSocketFactory that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            client = new OkHttpClient.Builder().sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            if(s.equals(myIPAddress)){
                                return true;
                            }else{
                                return false;
                            }
                        }
                    })
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
