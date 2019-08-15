public class SecurityHandler {

private static CloseableHttpClient client; 
private static PoolingHttpClientConnectionManager connPool = null;

public SecurityHandler()
{}  


public CloseableHttpClient getHttpClient(FilenetRequestCredentials authCredentials) throws Exception
{
    LoggingHandler.debug("In getHttpClient");

    // Create a trust manager that does not validate certificate chains             
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    } };


    javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("SSL");

    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
    SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext,
            SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);


    //SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() { @Override public boolean verify(final String s, final SSLSession sslSession) { return true; } }); 
    Registry r = RegistryBuilder. create() .register("https", factory).build(); 

    connPool = new PoolingHttpClientConnectionManager(r);
    connPool.setMaxTotal(Constants.MAX_POOL_CONNECTIONS); 
    //connPool.setDefaultMaxPerRoute(20);

    //Setting configurations for the request
    RequestConfig.Builder configBuilder = RequestConfig.custom()
                                        .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.KERBEROS))
                                        .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.KERBEROS));


    //Setting Credentials
    CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(AuthScope.ANY, new NTCredentials(authCredentials.getUsername(),authCredentials.getPassword(),authCredentials.getHostname(),authCredentials.getDomainName()));

    client = HttpClients.custom()
            .setDefaultRequestConfig(configBuilder.build())
            .setConnectionManagerShared(true)
            .setConnectionManager(connPool)
            .setSSLSocketFactory(factory)
            .setDefaultCredentialsProvider(credsProvider)
            .build();

    LoggingHandler.debug("Returning httpClient from getHttpClient.");


    return client;

}
