System.setProperty("javax.net.ssl.keyStore", "admin.jks");
System.setProperty("javax.net.ssl.keyStorePassword", "password");
makeClient().resource(getResetURI()).post(Response.class, RESET_DATA);
System.setProperty("javax.net.ssl.keyStore", "limited-user.jks");
makeClient().resource(getUpdateURI()).post(Response.class, TEST_UPDATE_SMALL_DATA); // this connection is actually as admin

...
    public Client makeClient() {
        final DefaultApacheHttpClientConfig clientConfig = new DefaultApacheHttpClientConfig();
        clientConfig.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);    
        clientConfig.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
        String keystore = Util.getSystemProperty("javax.net.ssl.keyStore");;
        String keystorePassword = Util.getSystemProperty("javax.net.ssl.keyStorePassword");
        try{
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            KeyStore ks = KeyStore.getInstance("JKS");
            try(FileInputStream fis = new java.io.FileInputStream(keystore)){
                ks.load(fis, keystorePassword.toCharArray());
            }
            kmf.init(ks, keystorePassword.toCharArray());
            SSLContext sslctx = SSLContext.getInstance("TLSv1.2");
            sslctx.getClientSessionContext().setSessionCacheSize(1);
            sslctx.getClientSessionContext().setSessionTimeout(1);
            // TODO: the below two nulls are probably insecure
            sslctx.init(kmf.getKeyManagers(), null, new SecureRandom());
            clientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(null, sslctx));
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }

        /* we must construct our own handler in order to override the Retry handler */
        final ApacheHttpClientHandler clientHandler = new ApacheHttpClientHandler(new HttpClient(new MultiThreadedHttpConnectionManager()), clientConfig);
        clientHandler.getHttpClient().getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));

    Client tclient = new ApacheHttpClient(clientHandler);
    tclient.setFollowRedirects(true);
    return tclient;
}
