SSLContext ctx = SSLContext.getInstance("TLS");
KeyStore keystore = KeyStore.getInstance("PKCS12");
keystore.load(new FileInputStream(new File("/data/local/tmp/admin.p12")), certpw.toCharArray());
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(keystore, certpw.toCharArray());
ctx.init(keyManagerFactory.getKeyManagers(),
                    new TrustManager[] { new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[] {};
                        }

                        @Override
                        public void checkClientTrusted(
                                final X509Certificate[] arg0,
                                final String arg1)
                                throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(
                                final X509Certificate[] arg0,
                                final String arg1)
                                throws CertificateException {

                        }
                    } }, new SecureRandom());
