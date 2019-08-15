try {
    KeyStore keyStore  = KeyStore.getInstance("JKS");
    java.io.FileInputStream fis = null;
    try {
        fis = new java.io.FileInputStream("mykeystore.jks");
        keyStore.load(fis, "password".toCharArray());
    } finally {
        if (fis != null) {
            fis.close();
        }
    }

    SSLContext sslcontext = SSLContexts.custom()
        .loadKeyMaterial(keyStore, "password".toCharArray())
        .loadTrustMaterial(null, new TrustSelfSignedStrategy())
        .build();

    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
        sslcontext,
        SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    CloseableHttpClient client = HttpClients.custom()
        .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
        .setSSLSocketFactory(sslsf)
        .build();           

    HttpGet httpGet = new HttpGet("https://test1.mobileticket.se/api/v1/auth/andreas");

    CloseableHttpResponse response = client.execute(httpGet);           
    String responseString = new BasicResponseHandler().handleResponse(response);

    //Close and return
    client.close();
}
catch(IOException e){
    log.info("IOException: " + e.getMessage());
}catch(Exception e){
    log.info("Exception: " + e.getMessage());
}
