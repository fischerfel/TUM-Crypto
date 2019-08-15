    final TrustManager[] certs = new TrustManager[]{new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] x509Certificates = new X509Certificate[0];
            return x509Certificates;
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain,
                                       final String authType) {
        }

        @Override
        public void checkClientTrusted(final X509Certificate[] chain,
                                       final String authType) {
        }
    }};

    SSLContext ctx = null;

    ctx = SSLContext.getInstance("TLS");
    ctx.init(null, certs, new SecureRandom());



    HttpUrlConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory();
