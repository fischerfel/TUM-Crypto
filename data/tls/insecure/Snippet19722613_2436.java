public void test() throws Exception {
    URL u = new URL(
            "https://my-server.com/my-webservices/data");
    HttpsURLConnection http = (HttpsURLConnection) u.openConnection();
    http.setSSLSocketFactory(createSSLContext().getSocketFactory());
    http.setAllowUserInteraction(true);
    http.setRequestMethod("GET");
    http.connect();

    InputStream is = http.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder stringBuilder = new StringBuilder();
    String line = null;
    while ((line = reader.readLine()) != null)
    {
        stringBuilder.append(line
                + "\n");
    }
    System.out.println(stringBuilder.toString());

}

private SSLContext createSSLContext() throws Exception {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    FileInputStream in = new FileInputStream("path_to_ca_file.pem");
    KeyStore trustStore = KeyStore.getInstance("JKS");
    trustStore.load(null);
    try {
        X509Certificate cacert = (X509Certificate) cf.generateCertificate(in);
        trustStore.setCertificateEntry("ca", cacert);
    } finally {
        IOUtils.closeQuietly(in);
    }

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(trustStore);

    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
    return sslContext;
}
