private static HttpClient getHttpClient(int timeout) {
    if (null == mHttpClient) {

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 

            HttpParams params = new BasicHttpParams();

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params,
                    HTTP.DEFAULT_CONTENT_CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, true);


            ConnManagerParams.setTimeout(params, timeout);

            HttpConnectionParams.setConnectionTimeout(params, timeout);

            HttpConnectionParams.setSoTimeout(params, timeout);


            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", sf, 443));

            ClientConnectionManager conManager = new ThreadSafeClientConnManager(
                    params, schReg);

            mHttpClient = new DefaultHttpClient(conManager, params);
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultHttpClient();
        }
    }
    return mHttpClient;
}
