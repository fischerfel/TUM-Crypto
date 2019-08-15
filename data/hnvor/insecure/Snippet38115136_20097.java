SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, new TrustManager[] { new X509TrustManager() {
    @Override
    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null; 
    }
} }, new SecureRandom());

SSLSocketFactory socketFactory = sslContext.getSocketFactory();
HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);

HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
conn.setHostnameVerifier(new HostnameVerifier() {
    @Override
    public boolean verify(String string, SSLSession ssls) {
        return true;
    }
});
