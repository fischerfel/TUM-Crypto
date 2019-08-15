SSLContext sc = SSLContext.getInstance("SSL");
                        TrustManager[] trustAllCerts = null;
                        sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
