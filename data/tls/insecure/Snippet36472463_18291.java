        URL url_uri = new URL(url);
        AssetManager am = context.getAssets();
        InputStream caInput = am.open("certs/myCert.bks");
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance("BKS");
            char[] pass = "MyPassword".toCharArray();
            keyStore.load(caInput, pass);
        } finally {
            caInput.close();
        }

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        HttpsURLConnection urlConnection =
                (HttpsURLConnection)url_uri.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());

        InputStream in = urlConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer sb = new StringBuffer("");
        String line = "";

        String NL = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null){
            sb.append(line + NL);
        }
        in.close();
        JSON = sb.toString();
