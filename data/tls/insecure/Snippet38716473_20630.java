TrustManager[] trustManager = new X509TrustManager[] { new X509TrustManager() {

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

SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(null, trustManager, null);

Client client = ClientBuilder.newBuilder().sslContext(sslContext).build();
