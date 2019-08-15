private static final String USER_AGENT = "Mozilla/5.0 (Linux; U; Android 4.0.3; et-ee; GT-P5100 Build/IML74K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30";

....

@Override
protected Object doInBackground(String... urlString) {
    ....
    URL url = new URL(urlString[0]);
    HttpsURLConnection urlConnection =(HttpsURLConnection)url.openConnection();
    urlConnection.setSSLSocketFactory(getSSLContext().getSocketFactory());
    urlConnection.addRequestProperty("User-Agent", USER_AGENT);
    urlConnection.setDoInput(true);   
    urlConnection.connect();
    ....
}

private SSLContext getSSLContext() throws CertificateException,IOException, KeyStoreException, NoSuchAlgorithmException,KeyManagementException, java.security.cert.CertificateException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream caRootInput = context.getAssets().open("MySSLCertificate_here.crt");
    InputStream caClientAuth = context.getAssets().open("MyCientCertificate_here");
    ....
    Certificate caRoot;
    caRoot = cf.generateCertificate(caRootInput);

    // KeyStore and KeyManager for Client Certificate
    KeyStore keystore_client = KeyStore.getInstance("BKS");
    keystore_client.load(caClientAuth, "My_passWord_here".toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keystore_client, "My_passWord_here".toCharArray());
    KeyManager[] keyManagers = kmf.getKeyManagers();

    // KeyStore and Trustmanager for SSL Certificate
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null, null);
    keyStore.setCertificateEntry("caRoot", caRoot);
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(keyStore);

    SSLContext context = SSLContext.getInstance("TLSv1");
    context.init(keyManagers, tmf.getTrustManagers(), null);
    return context;
    ....
    }
