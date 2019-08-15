        FileInputStream fIn = new FileInputStream("lamkin.p12");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(fIn, password);


        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, password);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        URL url = new URL("https://kompot.petrsu.ru/kani/");
        HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        InputStream in = urlConnection.getInputStream();
