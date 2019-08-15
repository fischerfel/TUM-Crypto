 public static Client IgnoreSSLClient() throws Exception {
    SSLContext sslcontext = SSLContext.getInstance("TLS");
    sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }

    }}, new java.security.SecureRandom());
    return ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier((s1, s2) -> true).build();
}
