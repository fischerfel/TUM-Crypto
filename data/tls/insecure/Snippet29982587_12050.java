HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManager[] tm = new TrustManager[] {
                    new X509TrustManager() {

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                            // not implemented
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                            // not implemented
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                    }
            };
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

        }catch(Exception e){}
