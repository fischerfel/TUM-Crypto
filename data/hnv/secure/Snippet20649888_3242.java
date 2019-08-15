private DefaultHttpClient createHttpClient() {
        DefaultHttpClient ret = new DefaultHttpClient(){
            /* (non-Javadoc)
             * @see org.apache.http.impl.client.DefaultHttpClient#createClientConnectionManager()
             */
            @Override
            protected ClientConnectionManager createClientConnectionManager() {
                SchemeRegistry registry = new SchemeRegistry();
                try {
                    URI uri;
                    uri = new URI(getURI());
                    if (Http.SCHEME_HTTP.equals(uri.getScheme())) {
                        registry.register(new Scheme(Http.SCHEME_HTTP, PlainSocketFactory.getSocketFactory(), 80));
                    } else if (Http.SCHEME_HTTPS.equals(uri.getScheme())) {
                        registry.register(new Scheme(Http.SCHEME_HTTPS, createSSLSocketFactory(), 443));
                    }
                } catch (URISyntaxException e) {
                    LogUtils.e(e.getMessage(), e);
                }
                return new SingleClientConnManager(getParams(), registry);
            }
            private SSLSocketFactory createSSLSocketFactory() {

                final Resources resources = mContext.getResources();
                final InputStream in = resources.openRawResource(R.raw.ippaps);

                ApplicationManeger apm = ApplicationManeger.getInstance();
                final char[] passwdchars = apm.getProperty(
                        PropertiesConstants.SSL_PASSWORD).toCharArray();

                SSLSocketFactory socketFactory = null;
                try {
                    KeyStore keyStore = KeyStore.getInstance("BKS");
                    try {
                        keyStore.load(in, passwdchars);
                    } finally {
                        in.close();
                    }
                    socketFactory = new SSLSocketFactory(keyStore);
                    socketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
                } catch (Exception e) {
                    LogUtils.d(e.getMessage(), e);
                }
                return socketFactory;
            }
        };

        return ret;
    }
