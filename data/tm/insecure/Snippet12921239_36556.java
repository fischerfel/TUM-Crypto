X509TrustManager trustManager = new X509TrustManager() {

    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
};
SSLContext sslcontext = SSLContext.getInstance("TLS");
KeyManager[] managers = /* Code that get the current client's KeyManager[] */;

sslcontext.init(managers, trustManager, null);
SSLSocketFactory socketFactory = new SSLSocketFactory(sslcontext);
socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

HttpParams params = new BasicHttpParams();
params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
HttpClient client = new DefaultHttpClient(params);
Scheme sch = new Scheme("https", socketFactory, 443);

client.getConnectionManager().getSchemeRegistry().register(sch);
HttpPost post = /* Code that get the POST */ 

HttpResponse response = client.execute(post, new BasicHttpContext());
