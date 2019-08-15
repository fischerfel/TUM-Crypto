TrustManager tm = new X509TrustManager() {
    public void checkClientTrusted(X509Certificate[] chain,
                    String authType)
                    throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public void checkServerTrusted(X509Certificate[] chain,
                    String authType)
                    throws CertificateException {
        // This will never throw an exception.
        // This doesn't check anything at all: it's insecure.
    }
};

final SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, new TrustManager[] {tm}, null);
Context context = client.getContext();
context.getAttributes().put("sslContextFactory", new SslContextFactory() {
    public void init(Series<Parameter> parameters) { }
    public SSLContext createSslContext() { return sslContext; }
});
