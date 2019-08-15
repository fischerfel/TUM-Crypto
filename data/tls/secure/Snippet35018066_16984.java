        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, null, null);

        HttpClientBuilder clientBuilder = HttpClientBuilder.create().setSslcontext(context);
        CloseableHttpClient httpClient = clientBuilder.build();
        HttpGet request = new HttpGet(new URI(baseURL));
        request.addHeader("Authorization", authHeader);

        CloseableHttpResponse httpResponse = httpClient.execute(request);
        json = EntityUtils.toString(httpResponse.getEntity());
