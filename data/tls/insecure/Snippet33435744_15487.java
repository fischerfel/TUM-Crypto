    String https_url = "https://localhost:443/myapp";
    URL url;
    try {

        url = new URL(https_url);
        HttpsURLConnection sslCon = (HttpsURLConnection) url.openConnection();


        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        FileInputStream stream = new FileInputStream("client.jks");
        keyStore.load(stream, "changeit".toCharArray());


        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, tmf.getTrustManagers(), null);
        SSLSocketFactory sslSocketFactory = ctx.getSocketFactory();

        sslCon.setSSLSocketFactory(sslSocketFactory);
        sslCon.setRequestMethod("GET");

        // add request header
        sslCon.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = sslCon.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(sslCon.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        System.out.println(response.toString());

    } catch (Exception e) {
        e.printStackTrace();
    }
