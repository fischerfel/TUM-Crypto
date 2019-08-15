TrustManager tm = new X509TrustManager() {
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
};

// Create a trust manager that does not validate certificate chains
TrustManager[] trustAllCerts = new TrustManager[] {
    new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
    }
};
SSLContext sslContext = null;

try {
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, new TrustManager[] { tm }, null);
} catch (Exception e1) {
    e1.printStackTrace();
    return;
}

AsyncSSLSocketMiddleware sslMiddleWare = Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware();
sslMiddleWare.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
sslMiddleWare.setSSLContext(sslContext);

Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);

Ion.with(context).load("POST", serverUrl)
    .setHeader("Content-Type", "application/json")
    .setHeader("Accept", "application/json")
    .setLogging("ION_LOGGING", Log.VERBOSE).setJsonObjectBody(json)
