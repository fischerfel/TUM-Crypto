DefaultHttpClient httpClient = new DefaultHttpClient();

X509HostnameVerifier verifier = new X509HostnameVerifier() {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }

    @Override
    public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {
    }

    @Override
    public void verify(String arg0, X509Certificate arg1) throws SSLException {
    }

    @Override
    public void verify(String arg0, SSLSocket arg1) throws IOException {
    }
};

private static class DefaultTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
    }

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}

SSLContext sslContext = SSLContext.getInstance("TLSv1");
sslContext.init(null, new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
SSLContext.setDefault(sslContext);

SSLSocketFactory sf = new SSLSocketFactory(sslContext,verifier);
ClientConnectionManager ccm = httpClient.getConnectionManager();
SchemeRegistry sr = ccm.getSchemeRegistry();
sr.register(new Scheme("https", sf, 9002));

HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
HttpParams params = new BasicHttpParams();
httpClient = new DefaultHttpClient(ccm, params);
