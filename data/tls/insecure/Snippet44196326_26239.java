  @SuppressLint("TrulyRandom")
        public static void handleSSLHandshake() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {

                        if(arg0.equalsIgnoreCase("google.com") || arg0.equalsIgnoreCase("firebasedynamiclinks.googleapis.com")||
                                arg0.equalsIgnoreCase("youtube.com")){
                            return true;
                        }else {
                            return false;
                        }
                    }
                });
            } catch (Exception ignored) {
            }
        }
