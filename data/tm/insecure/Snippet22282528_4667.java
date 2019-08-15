SSLContext trustAllSSLContext;

TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            try {
                trustAllSSLContext = SSLContext.getInstance("SSL");
                trustAllSSLContext.init(null, trustAllCerts, null);
            } catch (NoSuchAlgorithmException | KeyManagementException ex) {
                //...
            }
