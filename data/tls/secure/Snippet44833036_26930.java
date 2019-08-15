    if (restTemplate == null) {
        try {
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, null, null);
            CloseableHttpClient httpClient = HttpClientBuilder
                    .create()
                    .setSSLContext(context)
                    .build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
            restTemplate = new RestTemplate(factory);
        } catch (Exception e) {
            log.error("could not create SSL Context - cause::: {}", e.getMessage(), e);
            throw new CustomException(e);
        }
    }
