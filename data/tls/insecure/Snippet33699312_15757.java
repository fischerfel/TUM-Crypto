        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = new BufferedInputStream(context.getResources().openRawResource(R.raw.server));
        Certificate ca;

        ca = cf.generateCertificate(caInput);

        Log.d("CERT: ", "ca=" + ((X509Certificate) ca).getSubjectDN());

        caInput.close();

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        URL url = new URL("https://192.168.177.42:8443/");
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        InputStream in = urlConnection.getInputStream();
        in.close();
