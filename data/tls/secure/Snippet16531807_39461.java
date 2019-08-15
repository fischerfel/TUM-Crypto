char[] passphrase = "myComplexPass1".toCharArray();
KeyStore keystore = KeyStore.getInstance("BKS");
keystore.load(this.getApplicationContext().getResources().openRawResource(R.raw.jb), passphrase);
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(keystore, passphrase);
SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
Log.d("Context Protocol",sslContext.getProtocol());//this prints correctly TLS v1.2!
KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
TrustManager[] trustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers()
                        {
                            return null;
                        }
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                        {

                        }
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                        {

                        }
                    }
            };
sslContext.init(keyManagers, trustManagers, new SecureRandom());
SSLSocketFactory sslSocketFactory = (SSLSocketFactory) sslContext.getSocketFactory();
SSLSocket skt = (SSLSocket) sslSocketFactory.createSocket(HOST, PORT);
skt.setKeepAlive(true);
