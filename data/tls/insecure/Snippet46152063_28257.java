int timeout = 200000;
clientBuilder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
clientBuilder.readTimeout(timeout, TimeUnit.MILLISECONDS);
clientBuilder.writeTimeout(timeout, TimeUnit.MILLISECONDS);

X509TrustManager trustManager = new X509TrustManager() {
    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
};

final SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
sslSocketFactory = sslContext.getSocketFactory();
clientBuilder.sslSocketFactory(sslSocketFactory, trustManager);

clientBuilder.hostnameVerifier(new HostnameVerifier() {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
});
client = clientBuilder.build();
client.retryOnConnectionFailure();
