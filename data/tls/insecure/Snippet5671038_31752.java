TrustManager trm = new X509TrustManager() {
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public void checkClientTrusted(X509Certificate[] certs, String authType) {

    }

    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }
};

SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null, new TrustManager[] { trm }, null);
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
