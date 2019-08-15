    protected HttpsURLConnection createConnection(String url, Object extra) throws IOException {
    String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);

    // Load CAs from an InputStream
    // (could be from a resource or ByteArrayInputStream or ...)
    CertificateFactory cf = null;
    try {
        cf = CertificateFactory.getInstance("X.509");
    } catch (CertificateException e1) {

        // TODO Auto-generated catch block
        e1.printStackTrace();

    }
    // From https://www.washington.edu/itconnect/security/ca/load-der.crt
    InputStream caInput = new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory()
            + "/cub.crt"));
    Certificate ca = null;
    try {
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } catch (CertificateException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    } finally {
        caInput.close();
    }

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = null;
    try {
        keyStore = KeyStore.getInstance(keyStoreType);
        try {
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
        } catch (NoSuchAlgorithmException e1) {

            // TODO Auto-generated catch block
            e1.printStackTrace();

        } catch (CertificateException e1) {

            // TODO Auto-generated catch block
            e1.printStackTrace();

        }

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
        } catch (NoSuchAlgorithmException e1) {

            // TODO Auto-generated catch block
            e1.printStackTrace();

        }

        // Create an SSLContext that uses our TrustManager
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        try {
            context.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        HttpsURLConnection conn = (HttpsURLConnection) new URL(encodedUrl).openConnection();
        conn.setConnectTimeout(connectTimeout);
        conn.setSSLSocketFactory(context.getSocketFactory());
        conn.setReadTimeout(readTimeout);
        return conn;
    } catch (KeyStoreException e) {

        // TODO Auto-generated catch block
        e.printStackTrace();

    }
    return null;

}
