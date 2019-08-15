SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), new X509TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1.2" }, null,SSLConnectionSocketFactory.getDefaultHostnameVerifier());
Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();

HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(1, false);

RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(Integer.parseInt(30000)).setConnectTimeout(Integer.parseInt(30000)).setConnectionRequestTimeout(30000).setCookieSpec(CookieSpecs.STANDARD).build();

CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(defaultRequestConfig).setRetryHandler(retryHandler).evictExpiredConnections().build();

HttpPost httpPost = new HttpPost(<endpoint>);
httpPost.setEntity(new UrlEncodedFormEntity(requestData));
httpResponse = httpClient.execute(httpPost);
