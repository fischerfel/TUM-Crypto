public DefaultHttpClient getSSLClient(final String user, final String pwd)
        throws KeyManagementException, UnrecoverableKeyException,
        NoSuchAlgorithmException, KeyStoreException {

    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(null, new TrustManager[] { new CustomX509TrustManager() },
            null);

    HttpClient client = new DefaultHttpClient();

    SSLSocketFactory ssf = new CustomSSLSocketFactory(ctx, hostVerifier);
    ClientConnectionManager ccm = client.getConnectionManager();
    SchemeRegistry sr = ccm.getSchemeRegistry();
    sr.register(new Scheme("https", port, ssf));
    DefaultHttpClient sslClient = new DefaultHttpClient(ccm,
            client.getParams());

    sslClient.getCredentialsProvider().setCredentials(
            new AuthScope(host, port),
            new UsernamePasswordCredentials(user, pwd));

    /*
     * Those two lines are new and should force the client to use the ZAP proxy!
     */
    HttpHost proxy = new HttpHost("192.168.2.100", 8444, "https");
    sslClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

    return sslClient;
}
