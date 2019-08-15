SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, null, null);
HttpClientBuilder clientBuilder = HttpClientBuilder.create().setSslcontext(context);
HttpGet request = new HttpGet(baseURL);
request.addHeader("Authorization", basicAuthorization);
request.addHeader("Accept", "text/plain");
CloseableHttpClient httpClient = clientBuilder.build();
CloseableHttpResponse httpResponse = httpClient.execute(request);
