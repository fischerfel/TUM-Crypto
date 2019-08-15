    private SSLContext createSslContext() throws Exception {
        X509TrustManager tm = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] xcs,
                                       String string) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] xcs,
                                       String string) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[] { tm }, null);
        return ctx;
    }
