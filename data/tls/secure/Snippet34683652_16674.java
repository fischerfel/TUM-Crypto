public String getHTTPRequest(String baseURL, String authHeader) {

    logger.info("In getHTTPRequest");
    logger.info("Parms: URL = " + baseURL + " auth = " + authHeader);
    System.setProperty("javax.net.debug", "ssl:handshake:verbose");
    System.setProperty("jsse.enableSNIExtension", "false");

    String json = "";

    try {

        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, null, null);

        SSLConnectionSocketFactory sslCF = new SSLConnectionSocketFactory(context, new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    // or add your own test here
                    return true;
                }
            });

        //CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(sslCF).build();
        HttpClientBuilder clientBuilder = HttpClientBuilder.create().setSslcontext(context);
        CloseableHttpClient httpClient = clientBuilder.build();
        HttpGet request = new HttpGet(new URI(baseURL));
        request.addHeader("Authorization", authHeader);

        CloseableHttpResponse httpResponse = httpClient.execute(request);
        json = EntityUtils.toString(httpResponse.getEntity());
        System.out.println(" Response = " + json);

    } catch (Exception e) {
        logger.info(" Exception in getHTTPRequest = " + e.toString());
    }
    return json;
}
