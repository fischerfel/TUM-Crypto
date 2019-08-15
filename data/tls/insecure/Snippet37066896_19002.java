URL url = new URL("https://<yoururl>");

SSLContext sslCtx = SSLContext.getInstance("TLS");
sslCtx.init(null, new TrustManager[]{ new X509TrustManager() {

    private X509Certificate[] accepted;

    @Override
    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        accepted = xcs;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return accepted;
    }
}}, null);

HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

connection.setHostnameVerifier(new HostnameVerifier() {

    @Override
    public boolean verify(String string, SSLSession ssls) {
        return true;
    }
});

connection.setSSLSocketFactory(sslCtx.getSocketFactory());

if (connection.getResponseCode() == 200) {
    Certificate[] certificates = connection.getServerCertificates();
    for (int i = 0; i < certificates.length; i++) {
        Certificate certificate = certificates[i];
        File file = new File("/tmp/newcert_" + i + ".crt");
        byte[] buf = certificate.getEncoded();

        FileOutputStream os = new FileOutputStream(file);
        os.write(buf);
        os.close();
    }
}

connection.disconnect();
