public static HttpClient createHttpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    final SSLContext sslContext = SSLContext.getInstance("SSL");

    // set up a TrustManager that trusts everything
    sslContext.init(null, new TrustManager[] { new X509TrustManager() {
    }, new SecureRandom());

    final SSLSocketFactory sf = new SSLSocketFactory(sslContext);
    final Scheme httpsScheme = new Scheme("https", 443, sf);
    final SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(httpsScheme);

    final ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
    final HttpClient httpClient = new DefaultHttpClient(cm);
    return httpClient;
}
