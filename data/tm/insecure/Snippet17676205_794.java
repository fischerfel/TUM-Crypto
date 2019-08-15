public void execute(HttpContext httpContext, HttpUriRequest request, HttpHost proxy,
            Credentials proxyCredentials) throws IOException {
        HttpClient httpClient = getHttpClient(proxy, proxyCredentials, true, 
                configuration.getHttpConnectionTimeout(), configuration.getHttpSocketTimeout());
        httpClient.execute(request, httpContext);
    }

   /**
    * Default constructor
    */
    public HttpClientUtil() throws IOException {
            /*
             * A TrustManager which trusts every server certificates.
             */
            TrustManager tm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException { }
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException { }
            };
            try {
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(new KeyManager[0], new TrustManager[]{tm}, new SecureRandom());
                connectionManager = new ThreadSafeClientConnManager();
                connectionManager.setMaxTotal(Constant.HTTP_CONNECTION_POOL_MAX_TOTAL_CONNECTIONS);
                connectionManager.setDefaultMaxPerRoute(Constant.HTTP_CONNECTION_POOL_DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
                connectionManager.getSchemeRegistry().register(new Scheme(Constant.PROTOCOL_HTTPS, Constant.HTTPS_DEFAULT_PORT, new SSLSocketFactory(context)));
            } catch (Exception e) {
                throw new IOException(e);
            }
        }

private HttpClient getHttpClient(HttpHost proxy, Credentials proxyCredentials, 
            boolean followRedirects, int connectionTimeout, int soTimeout) {
        DefaultHttpClient client = new DefaultHttpClient(connectionManager);
        client.addRequestInterceptor(requestAcceptEncoding);
        client.addResponseInterceptor(httpResponseMaskInterceptor);
        client.addResponseInterceptor(responseContentEncoding);
        HttpParams params = client.getParams();
        if (proxy != null) {
            params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            if (proxyCredentials != null) {
                client.getCredentialsProvider().setCredentials(
                        new AuthScope(proxy.getHostName(), proxy.getPort()), proxyCredentials);
            }
        }
        HttpProtocolParams.setUserAgent(params, configuration.getUserAgent());
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
        HttpConnectionParams.setSoTimeout(params, soTimeout);
        HttpClientParams.setRedirecting(params, followRedirects);
        if (followRedirects) {
            client.setRedirectStrategy(redirectStrategy);
        }
        return client;
    }
