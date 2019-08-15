public void addRootCA() throws Exception {
    InputStream fis = new BufferedInputStream(this.getClassLoader().getResourceAsStream("letsencrypt.crt"));
    Certificate ca = CertificateFactory.getInstance("X.509").generateCertificate(fis);
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(null, null);
    ks.setCertificateEntry("LetsEncrypt CA", ca);
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ks);
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(null, tmf.getTrustManagers(), null);
    HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
}
