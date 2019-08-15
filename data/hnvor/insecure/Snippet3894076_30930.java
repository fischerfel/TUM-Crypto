// ...
SSLSocketFactory sf = new SSLSocketFactory (sslContext);
sf.setHostnameVerifier(new X509HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }

    public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
    }

    public void verify(String host, X509Certificate cert) throws SSLException {
    }

    public void verify(String host, SSLSocket ssl) throws IOException {
    }
});
// ...
