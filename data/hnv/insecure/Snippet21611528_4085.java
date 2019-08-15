RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(4000).setConnectTimeout(4000)
        .setSocketTimeout(4000).build();

CloseableHttpClient client = HttpClients.custom().setSslcontext(sslContext)
        .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
        .setDefaultRequestConfig(requestConfig).build();

HttpPost post = new HttpPost("https://localhost:" + connection.getLocalPort() + "/API/webservice.asmx");

StringEntity stringE = new StringEntity(REQUEST);
    post.setEntity(stringE);

CloseableHttpResponse response = client.execute(post);
