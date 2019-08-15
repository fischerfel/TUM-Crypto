TrustManager[] trustAllCertificates = new TrustManager[] {
    new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null; // Not relevant.
        }
        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
            // Do nothing. Just allow them all.
        }
        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
            // Do nothing. Just allow them all.
        }
    }
};

HostnameVerifier trustAllHostnames = new HostnameVerifier() {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true; // Just allow them all.
    }
};

try {
    System.setProperty("jsse.enableSNIExtension", "false");
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCertificates, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames);
}
catch (GeneralSecurityException e) {
    throw new ExceptionInInitializerError(e);
}
