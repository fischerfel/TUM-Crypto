        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = mcontext.getAssets().open("idashboard.cer");
        Certificate ca = null;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate)   ca).getSubjectDN());
        }catch (CertificateException e4) {
            e4.printStackTrace();
        }finally {
            try{
                caInput.close();}catch (Exception e5){
                e5.printStackTrace();
            }
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf =   TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        // Tell the URLConnection to use a SocketFactory from our SSLContext
        URL murl = new URL("My URL Here");
        HttpsURLConnection urlConnection =
                (HttpsURLConnection)murl.openConnection();
        urlConnection.setHostnameVerifier(new NullHostNameVerifier());
        urlConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        InputStream in = urlConnection.getInputStream(); //GETTING ERROR AT THIS LINE


        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            result.append(line);
        }
