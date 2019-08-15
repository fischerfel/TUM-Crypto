        try {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sslContext.init(null, new TrustManager[]{trustManager}, null);
        SslContextSecureProtocolSocketFactory socketFactory = new SslContextSecureProtocolSocketFactory(sslContext,false);
        Protocol.registerProtocol("https", new Protocol("https", (ProtocolSocketFactory) socketFactory, 443));//同样会影响到HttpUtils
    } catch (Throwable e) {
        e.printStackTrace();
