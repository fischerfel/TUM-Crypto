    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
            return myTrustedAnchors;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs,
                                       String authType) {}

        @Override
        public void checkServerTrusted(X509Certificate[] certs,
                                       String authType) {}
    } };
    try {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        SSLSocketFactory factory = sslContext.getSocketFactory();
        mWebSocketClient.setSocket(factory.createSocket());
        mWebSocketClient.connectBlocking();
    }
    catch (Exception e){
        e.printStackTrace();
    }
