DefaultHttpClient httpClient = new DefaultHttpClient();
Credentials credentials = new UsernamePasswordCredentials(LoginName,Password);
httpClient.getCredentialsProvider().setCredentials(org.apache.http.auth.AuthScope.ANY, credentials);
SSLContext ctx = SSLContext.getInstance("TLS");

X509TrustManager tm = new X509TrustManager() 
{
    public void checkClientTrusted(X509Certificate[] xcs, String string) 
    {}

    public void checkServerTrusted(X509Certificate[] xcs, String string) 
    {}

    public X509Certificate[] getAcceptedIssuers() 
    {
            return null;
    }
};

ctx.init(null, new TrustManager[] { tm }, null);
SSLSocketFactory ssf = new SSLSocketFactory(ctx);
ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", ssf, 443));

ClientExecutor clientExecutor = new ApacheHttpClient4Executor(httpClient);          
    ClientRequest request = new ClientRequest(clientURI + tokenURL, clientExecutor);
   request.getQueryParameters().add("loginName", LoginName);
    request.getQueryParameters().add("operatorId", OperatorId.toString());
    ClientResponse<String> response;

    try 
    {   
        response = request.get(String.class);

        if (response.getStatus() != Status.OK.getStatusCode())
        {
            System.out.println(("Failure: HTTP Code : " + response.getStatus()));
        }

        token = response.getEntity().toString();

    } catch (Exception e) 
    {
        logger.error(("Failed: Exception=" + e.getMessage()) +  " " + e);
    }
