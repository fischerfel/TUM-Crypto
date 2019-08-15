private static CloseableHttpClient appHttpClient() throws Exception {
    if (null != httpclient) {
        return httpclient;
    }
    final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
            .<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(trustAllSSLContext())).build();

    final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    cm.setMaxTotal(HTTP_MAX_CON);
    cm.setDefaultMaxPerRoute(HTTP_CON_ROUTE);

    final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CON_TO)
            .setConnectionRequestTimeout(HTTP_REQ_CON_TO).setSocketTimeout(HTTP_SO_TO).build();

    // final SSLConnectionSocketFactory factory = new
    // SSLConnectionSocketFactory(getSSLContext());

    httpclient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();

    return httpclient;
}

public static SSLContext trustAllSSLContext() throws Exception {
    if (sslContext != null) {
        return sslContext;
    }
    sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(null, null, null);
    System.out.println(
            "SSLContext :: Protocol :: " + sslContext.getProtocol() + " Provider :: " + sslContext.getProvider());
    return sslContext;
}
