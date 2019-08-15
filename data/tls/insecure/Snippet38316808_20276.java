   public void initCert() {
    try {
        Log.i("PARSE","initCert");

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        String yairCert = "-----BEGIN CERTIFICATE-----\n" +
                YOUR CERTIFICATE HERE
                "-----END CERTIFICATE-----\n";
        InputStream caInput = new ByteArrayInputStream(yairCert.getBytes());
        Certificate ca = null;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            try {
                caInput.close();
            } catch (IOException e) {
                Log.e("PARSE_BUG","Failure on Cert installing",e);
                e.printStackTrace();
            }
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

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        Log.i("PARSE","Initiating Self Signed cert");
        okHttpClient.setSslSocketFactory(context.getSocketFactory());
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            Log.e("PARSE_BUG","Failure on Cert installing",e);
            e.printStackTrace();
        }
    } catch (IOException e) {
        Log.e("PARSE_BUG","Failure on Cert installing",e);
        e.printStackTrace();
    } catch (CertificateException e) {
        Log.e("PARSE_BUG","Failure on Cert installing",e);
        e.printStackTrace();

    } catch (NoSuchAlgorithmException e) {
        Log.e("PARSE_BUG","Failure on Cert installing",e);
        e.printStackTrace();
    } catch (KeyStoreException e) {
        Log.e("PARSE_BUG","Failure on Cert installing",e);
        e.printStackTrace();
    } catch (KeyManagementException e) {
        Log.e("PARSE_BUG","Failure on Cert installing",e);
        e.printStackTrace();
    }
