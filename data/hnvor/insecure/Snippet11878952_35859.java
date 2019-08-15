                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null /*keyManagers*/, trustAllCerts,
                          new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();


                // Don't check the hostname against the certificate name
                conn.setHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String urlHostname,
                            SSLSession session) {
                            return true;
                        }
                    });
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("METHOD", "POST");
                conn.setRequestProperty("Authorization", "Basic " +
                    credentials);
                conn.setRequestProperty("Content-Type", "application/pkcs10");
                conn.setReadTimeout(8000);
                conn.connect();
