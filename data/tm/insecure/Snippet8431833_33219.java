TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public void checkClientTrusted(X509Certificate[] certs, String authType) {
        return;
    }

    public void checkServerTrusted(X509Certificate[] certs, String authType) {
        return;
    }
}};

sslContext.init(kmf.getKeyManagers(), trustAllCerts, new SecureRandom());
