 CertificateFactory cf = CertificateFactory.getInstance("X.509","BC");
        Certificate ca;
InputStream caInput =new URL("http://cacerts.digicert.com/DigiCertSHA2HighAssuranceServerCA.crt").openStream();

        ca = cf.generateCertificate(caInput);
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

        URL url = new URL("https://kolkatatrafficpolice.net");
        HttpsURLConnection urlConnection =
                (HttpsURLConnection)url.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        InputStream in = urlConnection.getInputStream();
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
