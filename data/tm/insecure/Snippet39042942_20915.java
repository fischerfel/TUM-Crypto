public static HttpsURLConnection getNewURLConnection() {

        String[] uriArr = Config.get().getUriArr();
        HttpsURLConnection conn = null;

        for (int i = 0; i < uriArr.length; i++) {

            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            try {

                SSLContext ssl = SSLContext.getInstance("SSL");
                ssl.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory());

                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                URL url = new URL(uriArr[i]);
                conn = (HttpsURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                int timeoutSocket = 20000;
                conn.setReadTimeout(timeoutSocket);

                int timeoutConnection = 10000;
                conn.setConnectTimeout(timeoutConnection);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);

            } catch (Exception e) {
                Log.e(TAG, "http client creation failed");
                conn = null;
            }

            if (conn != null)
                break;

        }

        return conn;
    }
