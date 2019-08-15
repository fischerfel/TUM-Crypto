SSLSocketFactory sslSocketFactory = null;

        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            // Initialise the TMF as you normally would, for example:
            try {
                tmf.init((KeyStore)null);
            } catch(KeyStoreException e) {
                e.printStackTrace();
            }
            TrustManager[] trustManagers = tmf.getTrustManagers();

            final X509TrustManager origTrustmanager = (X509TrustManager)trustManagers[0];

            // Create a trust manager that does not validate certificate chains
            TrustManager[] wrappedTrustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return origTrustmanager.getAcceptedIssuers();
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            try {
                                origTrustmanager.checkClientTrusted(certs, authType);
                            } catch(CertificateException e) {
                                e.printStackTrace();
                            }
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            try {
                                origTrustmanager.checkServerTrusted(certs, authType);
                            } catch(CertificateException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            };
            //TrustManager[] trustAllCerts = TrustManagerFactory.getInstance("SSL").getTrustManagers();

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, wrappedTrustManagers, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
