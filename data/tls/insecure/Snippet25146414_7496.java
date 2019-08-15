public SSLContext getTrusted() throws Exception{
        // Load CAs from an InputStream
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        AssetManager assManager = context.getAssets();
        InputStream is = null;
        try {
                is = assManager.open("ca.cert.crt");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        InputStream caInput = new BufferedInputStream(is);

        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            Log.d("TrustMan", "ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
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

        return context;
    }
