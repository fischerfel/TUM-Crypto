public static List<Cookie> cookies;
public DefaultHttpClient getHttpclient() {
        if (!isHTTPSEnabled()) {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeOut);
            HttpConnectionParams.setSoTimeout(httpParameters, timeOut);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpClientParams.setRedirecting(httpclient.getParams(), false);

            if (cookies != null) {
                int size = cookies.size();
                for (int i = 0; i < size; i++) {
                    httpclient.getCookieStore().addCookie(cookies.get(i));
                }
            }
            httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android");
            return httpclient;
        } else {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeOut);
                HttpConnectionParams.setSoTimeout(httpParameters, timeOut);
                HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParameters, registry);
                DefaultHttpClient httpclient = new DefaultHttpClient(ccm, httpParameters);
                HttpClientParams.setRedirecting(httpclient.getParams(), false);

                if (cookies != null) {
                    int size = cookies.size();
                    for (int i = 0; i < size; i++) {
                        httpclient.getCookieStore().addCookie(cookies.get(i));
                    }
                }
                httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android");
                return httpclient;

            } catch (Exception e) {
            }
        }
        return null;

    }
