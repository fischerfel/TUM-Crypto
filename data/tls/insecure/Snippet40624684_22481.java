SSLContext context = null;
    try 
    {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // Get the raw resource, which contains the keystore with
        // your trusted certificates (root and any intermediate certs)
        InputStream input = new BufferedInputStream(context.getAssets().open(pkcsFilename));
        try {
            // Initialize the keystore with the provided trusted certificates
            // Also provide the password of the keystore
            keyStore.load(input, password.toCharArray());
        } finally {
            input.close();
        }

        KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyFactory.init(keyStore, "".toCharArray());

        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca = null;
        input = new BufferedInputStream(context.getAssets().open(certificateFilename));
        try 
        {
            ca = cf.generateCertificate(input);
        }
        finally
        {
            input.close();
        }
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        trustStore.setCertificateEntry("server", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        // Create an SSLContext that uses our TrustManager
        context = SSLContext.getInstance("TLS");
        context.init(keyFactory.getKeyManagers(), tmf.getTrustManagers(), null);

        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
