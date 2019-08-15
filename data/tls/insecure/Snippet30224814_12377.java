HttpsURLConnection.setDefaultHostnameVerifier(getUnsecureHostNameVerifier());

try {
        SSLContext e = SSLContext.getInstance("TLS");
        e.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
        SSLContext.setDefault(e);
        HttpsURLConnection.setDefaultSSLSocketFactory(e.getSocketFactory());
    } catch (Exception var1) {
        throw new Exception("SSL Error", var1);
    }

 public static class DefaultTrustManager implements X509TrustManager {
    public DefaultTrustManager() {
    }

    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
