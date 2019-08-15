        URL url = new URL("https://ws.service");

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        InputStream keyInput = new FileInputStream("path_to_p12_file");
        keyStore.load(keyInput, p12password.toCharArray());
        keyInput.close();

        keyManagerFactory.init(keyStore, p12password.toCharArray());

        SSLContext context = SSLContext.getInstance("SSL");
        context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setSSLSocketFactory(context.getSocketFactory());
        con.connect();
