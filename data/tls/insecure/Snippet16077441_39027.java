    try {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String name) throws CertificateException {}

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String name) throws CertificateException {}

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } }, new SecureRandom());

        SSLContext.setDefault(ctx);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
