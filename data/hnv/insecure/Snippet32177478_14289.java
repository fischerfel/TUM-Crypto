        //Inserted cert into truststore because unsigned
        System.setProperty("javax.net.ssl.trustStore", "truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");

        DefaultHttpClient httpClient = new DefaultHttpClient();

        //Because connecting through ip hostname verification would fail
        SSLSocketFactory socketFactory = (SSLSocketFactory) httpClient.getConnectionManager().getSchemeRegistry().get("https").getSchemeSocketFactory();
        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpGet httpRequest = new HttpGet("https://<ip>:<port>/test");
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        System.out.println(httpResponse);
