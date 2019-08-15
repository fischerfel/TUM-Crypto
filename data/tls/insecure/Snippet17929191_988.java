    final SSLContext sslCtx;
    sslCtx = SSLContext.getInstance("SSL");
    sslCtx.init(null, new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] cert,
                    String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] cert,
                    String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }}, null);

    X509HostnameVerifier verifier = new X509HostnameVerifier() {
        @Override
        public void verify(String string, SSLSocket ssls) throws IOException {
        }

        @Override
        public void verify(String string, X509Certificate xc) throws SSLException {
        }

        @Override
        public void verify(String string, String[] strings, String[] strings1) throws SSLException {
        }

        @Override
        public boolean verify(String string, SSLSession ssls) {
            return true;
        }
    };
    final SSLSocketFactory socketFactory = new SSLv3SocketFactory(sslCtx, verifier);
    final SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("https", 443, socketFactory));

    final PoolingClientConnectionManager cm = new PoolingClientConnectionManager(registry);
    cm.setMaxTotal(100);
    cm.setDefaultMaxPerRoute(50);
    final HttpParams httpParams = new BasicHttpParams();
    HttpConnectionParams.setSoTimeout(httpParams, timeout);

    httpClient = new DefaultHttpClient(cm, httpParams);

    ((DefaultHttpClient) httpClient).setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
        @Override
        public long getKeepAliveDuration(HttpResponse hr, HttpContext hc) {
            return 0;
        }
    });
    httpClient.getParams().setParameter("http.socket.timeout", 900000);
