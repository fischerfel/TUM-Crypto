    KeyStore keyStore  = KeyStore.getInstance("PKCS12");
    FileInputStream instream = new FileInputStream(new File("client-p12-keystore.p12"));
    try {
        keyStore.load(instream, "helloworld".toCharArray());
    } finally {
        instream.close();
    }

    // Trust own CA and all self-signed certs
    SSLContext sslcontext = SSLContexts.custom()
        .loadKeyMaterial(keyStore, "helloworld".toCharArray())
        //.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()) //custom trust store
        .build();
    // Allow TLSv1 protocol only
    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
        sslcontext,
        new String[] { "TLSv1" },
        null,
        SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); //TODO
    CloseableHttpClient httpclient = HttpClients.custom()
        .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) //TODO
        .setSSLSocketFactory(sslsf)
        .build();
    try {

        HttpGet httpget = new HttpGet("https://localhost:8443/secure/index");

        System.out.println("executing request" + httpget.getRequestLine());

        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            HttpEntity entity = response.getEntity();

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
            }
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
    } finally {
        httpclient.close();
    }
