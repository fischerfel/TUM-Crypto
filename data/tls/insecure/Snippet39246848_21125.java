  CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");

                    InputStream caInput = new BufferedInputStream(new FileInputStream(certFile));
                       X509Certificate ca =(X509Certificate) cf.generateCertificate(caInput);
                    String keyStoreType = KeyStore.getDefaultType();
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null, null);
                    keyStore.setCertificateEntry("ca", ca);

                    // Create a TrustManager that trusts the CAs in our KeyStore
                    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                    tmf.init(keyStore);
                    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }

                        public void checkClientTrusted(X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        public void checkServerTrusted(X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }
                    }};
                    // Create an SSLContext that uses our TrustManager
                    HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
                    SSLContext sslcontext = SSLContext.getInstance("TLS");
                    sslcontext.init(null, trustAllCerts, new java.security.SecureRandom());

                    // Tell the URLConnection to use a SocketFactory from our SSLContext
                    url = new URL(wsdlUrl);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    urlConnection.setSSLSocketFactory(sslcontext.getSocketFactory());
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.connect();

                    if (urlConnection.getResponseCode() == 200) {  //Successful response.
                        result = true;
                    } else {
                        result = false;
                    }
