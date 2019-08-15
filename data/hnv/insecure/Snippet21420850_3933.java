    public static DefaultHttpClient createProxyHttpClient() {
        try {
            final DefaultHttpClient client = createPlaintHttpClient();
            client.setRoutePlanner(new HttpRoutePlanner() {
                @Override
                public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
                    boolean isSecure = "https".equalsIgnoreCase(target.getSchemeName());
                    if (needProxy) {
                        Header header = isSecure ? ProxyUtils.createHttpsAuthHeader() : ProxyUtils.createAuthHeader();
                        if (isSecure) {
                            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, com.netease.cloudmusic.utils.HttpRequest.USER_AGENT + "\r\n" + header.getName() + ":" + header.getValue());
                        } else {
                            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, com.netease.cloudmusic.utils.HttpRequest.USER_AGENT);
                            if (request instanceof RequestWrapper) {
                                request = ((RequestWrapper) request).getOriginal();
                            }
                            request.setHeader(header);
                        }
                        String host = isSecure ? ProxyUtils.SECURE_HOST : ProxyUtils.HOST;
                        int port = isSecure ? ProxyUtils.SECURE_PORT : ProxyUtils.PORT;
                        return new HttpRoute(target, null,  new HttpHost(host, port), isSecure);
                    } else {
                        client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, com.netease.cloudmusic.utils.HttpRequest.USER_AGENT);
                        return new HttpRoute(target, null, isSecure);
                    }
                }
            });
            return client;
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultHttpClient();
        }
    }

public static DefaultHttpClient createPlaintHttpClient() {
       try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            PlainSSLSocketFactory socketFactory = new PlainSSLSocketFactory(trustStore);
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            BasicHttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 30000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", socketFactory, 443));
            ThreadSafeClientConnManager ccm = new ThreadSafeClientConnManager(params, registry);
            HttpClientParams.setCookiePolicy(params, CookiePolicy.BROWSER_COMPATIBILITY);
            final DefaultHttpClient client = new DefaultHttpClient(ccm, params);
            client.setRoutePlanner(new HttpRoutePlanner() {
        @Override
        public HttpRoute determineRoute(HttpHost target, HttpRequest arg1, HttpContext arg2) throws HttpException {
               client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, com.netease.cloudmusic.utils.HttpRequest.USER_AGENT);
            return new HttpRoute(target, null, "https".equalsIgnoreCase(target.getSchemeName()));
        }
        });
            return client;
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultHttpClient();
        }
}
