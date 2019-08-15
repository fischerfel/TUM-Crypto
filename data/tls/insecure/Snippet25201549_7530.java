SSLContext ctx = SSLContext.getInstance("TLS");
InputStream is = new FileInputStream("/data/local/tmp/admin.cer");
CertificateFactory cf = CertificateFactory.getInstance("X.509");
Certificate caCert = (Certificate)cf.generateCertificate(is);

KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(null, certpw.toCharArray());
ks.setCertificateEntry("caCert", caCert);

KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(ks, certpw.toCharArray());

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
