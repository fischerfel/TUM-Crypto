public static class UniqueHttpClient {

    private static ThreadLocal<DefaultHttpClient> uniquelocal =

    new ThreadLocal<DefaultHttpClient>() {

        @Override
        protected DefaultHttpClient initialValue() {

            BasicHttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams
                    .setConnectionTimeout(httpParameters, 90000);

            HttpConnectionParams.setSoTimeout(httpParameters, 90000);

            DefaultHttpClient client;
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

            ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(
                    httpParameters, registry);

            client = new DefaultHttpClient(manager, httpParameters);

            return client;
        }

    };

    public static DefaultHttpClient getUniqueHttpClient() {

        return uniquelocal.get();
    }

} // class UniqueHttpClient
