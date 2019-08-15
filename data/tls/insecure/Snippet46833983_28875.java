private SSLSocketFactory getSocketFactory() {

CertificateFactory cf = null;
try {
    cf = CertificateFactory.getInstance("X.509");
    InputStream caInput = mContext.getResources().openRawResource(OUR_CERT);
    Certificate ca;
    try {
        ca = cf.generateCertificate(caInput);
        Log.e("CERT", "ca=" + ((X509Certificate) ca).getSubjectDN());
    } finally {
        caInput.close();
    }


    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);


    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);


    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {

            Log.e("CipherUsed", session.getCipherSuite());
            return hostname.compareTo("OUR_SERVER_HOSTNAME")==0;

        }
    };


    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    SSLContext context = null;
    context = SSLContext.getInstance("TLS");

    context.init(null, tmf.getTrustManagers(), null);
    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

    SSLSocketFactory sf = context.getSocketFactory();


    return sf;

} catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException | KeyManagementException e) {
    e.printStackTrace();
}

return  null; }
