    private SSLContext temporarySslSolution() {
    SSLContext sc = null;
    try {
        sc = SSLContext.getInstance("TLS");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    try {
        sc.init(null, trustAllCerts, new SecureRandom());
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }

    HttpsURLConnection.setDefaultHostnameVerifier(new RelaxedHostNameVerifier());
    return sc;

}

public static class RelaxedHostNameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}

public TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[] {};
    }
    public void checkClientTrusted(X509Certificate[] chain,
                                   String authType) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain,
                                   String authType) throws CertificateException {
    }
} };
