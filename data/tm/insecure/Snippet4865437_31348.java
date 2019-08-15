HttpsURLConnection
                .setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname,
                            SSLSession session) {
                        return true;
                    }
                });
        SSLContext context = SSLContext.getInstance("TLS"); // TLS
        context.init(null, new X509TrustManager[] { new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        } }, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());
