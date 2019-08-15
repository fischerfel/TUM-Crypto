        // Load CAs from our reference to the file
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = new BufferedInputStream(new FileInputStream(serverCert));
        X509Certificate serverCertificate;

        try {
            serverCertificate = (X509Certificate)cf.generateCertificate(caInput);
            System.out.println("ca=" + serverCertificate.getSubjectDN());
        } finally {
            caInput.close();
        }
        Log.d(TAG, "Server Cert: " + serverCertificate);

        // Create a KeyStore containing our trusted CAs
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);
        trustStore.setCertificateEntry("my ca", serverCertificate);

        //Load the Client certificate in the keystore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new FileInputStream(clientCert);
        keyStore.load(fis,CLIENT_PASSWORD);

        // Create a TrustManager that trusts the CAs in our KeyStore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        //Build the SSL Context
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, pref.getString(Constants.clientCertificatePassword, "").toCharArray

());


    //Create the SSL context
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
...
    //And later, we use that sslContext to initiatize the socketFactory

                urlConnection = (HttpsURLConnection) requestedUrl.openConnection();
         urlConnection.setSSLSocketFactory(CertificateManager.getInstance().getSslContext().getSocketFactory());
...
