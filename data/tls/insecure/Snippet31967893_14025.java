public int sendGetHTTP() throws QAException, IOException {
    HttpResponse httpResponse = null;
    try {
        DefaultHttpClient client = new DefaultHttpClient();
        InputStream is = new FileInputStream("my");

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate caCert = (X509Certificate)cf.generateCertificate(is);

        TrustManagerFactory tmf = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null);
        ks.setCertificateEntry("cert", caCert);

        tmf.init(ks);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        SSLSocketFactory sf = new SSLSocketFactory(sslContext);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme scheme = new Scheme("https", sf, 444);
        client.getConnectionManager().getSchemeRegistry().register(scheme);
        client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        HttpGet httpGet = new HttpGet("https://mysite:444/en.html");
        httpGet.addHeader("SSO-EMPLOYEENUMBER", "1234");
        httpResponse = client.execute(httpGet);
    } catch (Exception e) {
        e.printStackTrace();
    }
    int status = httpResponse.getStatusLine().getStatusCode();
    if (status != HTTP_STATUS_OK && status != HTTP_STATUS_CREATED) {
        throw new QAException("Server Response: " + status + ": " + httpResponse.getStatusLine().getReasonPhrase());
    }
    return status;
}
