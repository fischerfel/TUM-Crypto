TrustManager[] trustEverything = new TrustManager[] {
    new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() { return null; }
        public void checkClientTrusted(X509Certificate[] certs, String authType) { }
        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
    }
};

SSLContext sslctx = SSLContext.getInstance("SSL");
sslctx.init(null, trustEverything, null);

HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
