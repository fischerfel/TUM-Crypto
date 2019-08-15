   protected void sendFile(String url, File file)
{
    try
    {
        HttpClient httpclient = new DefaultHttpClient();

        httpclient = sslClient(httpclient);

        CredentialsProvider credProvider = new BasicCredentialsProvider();
        credProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
            new UsernamePasswordCredentials("YOUR USER NAME HERE", "YOUR PASSWORD HERE"));

        ((AbstractHttpClient) httpclient).setCredentialsProvider(credProvider);

        HttpPost httppost = new HttpPost(url);

        InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(true); // Send in multiple parts if needed
        httppost.setEntity(reqEntity);

        HttpResponse response = httpclient.execute(httppost);
        int status = response.getStatusLine().getStatusCode();
        // Do something with response...
        Log.d(CLASSNAME, "Response: " + Integer.toString(status) + response.toString());

    }
    catch(Exception e)
    {
        Log.e(CLASSNAME, "Caught exception: " + e.getMessage());
        e.printStackTrace();
    }
}

private HttpClient sslClient(HttpClient client) {
    try {
        X509TrustManager tm = new X509TrustManager() { 
            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new MySSLSocketFactory(ctx);
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = client.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", ssf, 443));
        return new DefaultHttpClient(ccm, client.getParams());
    } catch (Exception ex) {
        return null;
    }
}
