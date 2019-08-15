        URL url;
        HttpsURLConnection connection = null;
        try {
            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(getAssets().open("my_certificate.crt"));
            Certificate ca = cf.generateCertificate(caInput);

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

            String urlParameters = SOME_PARAMETERS;
            url = new URL(SOME_URL);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(context.getSocketFactory());

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");
            connection.setFixedLengthStreamingMode(urlParameters.getBytes().length);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(urlParameters);
            out.close();
