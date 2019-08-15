protected Certificate[] getCertificates(String url) throws IOException, GeneralSecurityException
{
    URL urlConnection = new URL(url);
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(new KeyManager[0], new TrustManager[] {new DummyTrustManager()}, new SecureRandom());
    SSLContext.setDefault(context);

    HttpsURLConnection con = (HttpsURLConnection)urlConnection.openConnection();
    con.setHostnameVerifier(
            new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                    }
    });

    con.setSSLSocketFactory(context.getSocketFactory());
    con.connect();
    Certificate[] certificates = con.getServerCertificates();

    con.disconnect();
    return certificates;

}

protected void storeCertificates(Certificate[] certs, String keystorePath, String keystorePassword) throws GeneralSecurityException, IOException
{
    KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    keystore.load(null, keystorePassword.toCharArray());
    for (Certificate c: certs)
    {
        keystore.setCertificateEntry(Integer.toHexString(c.hashCode()), c);
    }
    File f = new File(keystorePath);
    FileOutputStream out_stream = new FileOutputStream(f);
    keystore.store(out_stream, keystorePassword.toCharArray());
    out_stream.close();
}
