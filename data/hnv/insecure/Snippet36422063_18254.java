public static class UniqueHttpClient {

    private static ThreadLocal<HttpURLConnection> uniquelocal =

            new ThreadLocal<HttpURLConnection >() {

                @Override
                protected HttpURLConnection  initialValue() {

                    HttpURLConnection  urlConn = null;
                    try {
                        URL requestUrl = new URL(url);
                        urlConn = (HttpURLConnection) requestUrl.openConnection();
                        urlConn.setConnectTimeout(90000);
                        urlConn.setReadTimeout(90000);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // registers schemes for both http and httpsb

                    SchemeRegistry registry = new SchemeRegistry();

                    registry.register(new Scheme("http", PlainSocketFactory
                            .getSocketFactory(), 80));

                    KeyStore trustStore;

                    SSLSocketFactory sslSocketFactory;

                    try {

                        trustStore = KeyStore
                                .getInstance(KeyStore.getDefaultType());

                        trustStore.load(null, null);

                        sslSocketFactory = new MySSLSocketFactory(trustStore);

                    } catch (Exception exEXCP) {

                        exEXCP.printStackTrace();

                        sslSocketFactory = SSLSocketFactory.getSocketFactory();
                    }

                    sslSocketFactory
                            .setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                    registry.register(new Scheme("https", sslSocketFactory, 443));

                    //client = new DefaultHttpClient(manager, httpParameters);

                    return urlConn;
                }

            };
