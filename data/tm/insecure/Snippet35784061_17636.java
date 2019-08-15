private static DefaultHttpClient createHttpClient()
        throws NoSuchAlgorithmException, KeyManagementException {

    DefaultHttpClient defaultHttpClient = new DefaultHttpClient();

    defaultHttpClient.getParams().setParameter(
            ConnRoutePNames.DEFAULT_PROXY, proxy);

    defaultHttpClient = makeTrustfulHttpClient(defaultHttpClient);
    defaultHttpClient.addRequestInterceptor(
            new DiadocPreemptiveAuthRequestInterceptor(), 0);
    return defaultHttpClient;
}

private static DefaultHttpClient makeTrustfulHttpClient(HttpClient base)
        throws NoSuchAlgorithmException, KeyManagementException {



    SSLSocketFactory ssf = getTrustfulSslSocketFactory();
    ClientConnectionManager ccm = base.getConnectionManager();
    ccm.getSchemeRegistry().register(new Scheme("https", 443, ssf));
    return new DefaultHttpClient(ccm, base.getParams());
}

private static SSLSocketFactory getTrustfulSslSocketFactory()
        throws NoSuchAlgorithmException, KeyManagementException {


    SSLContext ctx = SSLContext.getInstance("TLS");
    X509TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] xcs, String string)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] xcs, String string)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    ctx.init(null, new TrustManager[] { tm }, null);


    return new SSLSocketFactory(ctx,
            SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


}
