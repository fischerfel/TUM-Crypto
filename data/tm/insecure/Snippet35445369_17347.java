TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
                };

          try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                        sc);
                httpclient = HttpClients.custom().setSSLSocketFactory(
                        sslsf).build();
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
