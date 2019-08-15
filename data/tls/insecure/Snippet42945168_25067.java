 allowTrustedSSL(this.activity);

 JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method,url,dataToSend, onResponseListener,onErrorListener);

/**
 * Method implements self-signed certificates
 * @param context
 */
public static void allowTrustedSSL(Context context){

    /**
     * We shall accept traffic with any host names
     */
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            Logger.d(TAG, "verify() called with: hostname = [" + hostname + "], session = [" + session + "]");
            return true;
        }
    });
    try{
        //the NoSSLv3SocketFactory will disable the SSLv3 
        SSLSocketFactory noSSLv3Factory = new NoSSLv3SocketFactory(sslSocketFactoryGenerator(context));
        HttpsURLConnection.setDefaultSSLSocketFactory(noSSLv3Factory);
    } catch (Exception e) {
        Logger.e(TAG, "allowTrustedSSL: "+e.getMessage());
        e.printStackTrace();
    }

}
public static SSLSocketFactory sslSocketFactoryGenerator(Context context) throws
        UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException,
        KeyStoreException, IOException, KeyManagementException {
    //the test.crt file 
    TrustManagerFactory trustManagerFactory = getTrustManagerFactory(context.getResources().openRawResource(R.raw.test));
    //the mykey.cer file
    KeyManagerFactory keyManagerFactory = getKeyManagerFactory(context.getResources().openRawResource(R.raw.mykey),
            MYKEY_PASS.toCharArray());

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),null);

    return sslContext.getSocketFactory();
}

public static TrustManagerFactory getTrustManagerFactory(InputStream fileInput) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
    // Load CAs from an InputStream
    // (could be from a resource or ByteArrayInputStream or ...)
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    Certificate ca;
    try {
        ca = cf.generateCertificate(fileInput);
        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
    } finally {
        fileInput.close();
    }

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    // Create a TrustManager that trusts the CAs in our KeyStore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    return tmf;
}

public static KeyManagerFactory getKeyManagerFactory(InputStream fileInput,char[] password)
        throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
    // Load CAs from an InputStream
    // (could be from a resource or ByteArrayInputStream or ...)
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    Certificate ca;
    try {
        ca = cf.generateCertificate(fileInput);
        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
    } finally {
        fileInput.close();
    }

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    // Create a TrustManager that trusts the CAs in our KeyStore
    String keyManAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(keyManAlgorithm);
    keyManagerFactory.init(keyStore, password);
    return  keyManagerFactory;
}
