SSLContext context = SSLContext.getInstance("TLSv1.2");
context.init(null, null, null);
CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(context).build();
HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
RestTemplate restTemplate = new RestTemplate(factory);
