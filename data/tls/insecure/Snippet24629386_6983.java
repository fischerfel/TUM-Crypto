public HttpClient myHttpsClient() {
    HttpClient client = null;
    char[] passphrase = "password".toCharArray();

    try {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        CertificateFactory clientcf = CertificateFactory.getInstance("X.509");
        InputStream caInput = context.getResources().openRawResource(R.raw.server);
        InputStream clientcert = context.getResources().openRawResource(R.raw.client);
        Certificate ca;
        Certificate clientca;
        try {
            clientca = clientcf.generateCertificate(clientcert);
            ca = cf.generateCertificate(caInput);
            System.out.println("ca="+ ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
            clientcert.close();
        }
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStoreclient = KeyStore.getInstance(keyStoreType);
        keyStoreclient.load(null, null);
        keyStoreclient.setCertificateEntry("ca", clientca);


        // Create a KeyStore containing our trusted CAs
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);



        String kmfAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(kmfAlgorithm);
            kmf.init(keyStoreclient,passphrase);



        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        MySSLSocketFactory socketFactory = new MySSLSocketFactory(context);//,new BrowserCompatHostnameVerifier());

        client = createHttps(socketFactory);
    } catch (Exception e) {
        e.printStackTrace();
    }

    return client;
