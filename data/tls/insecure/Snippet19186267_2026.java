(SSL = true, Proxy = false  : Success)
(SSl = false, Proxy = true : Success)
(SSL = true, Proxy = true : Fail (class javax.net.ssl.SSLException))



ServiceStub objStub = new ServiceStub(sWebServiceURL);
　　　　　　objStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
        objStub._getServiceClient().getOptions().setProperty("customCookieID",DEF_COOKIEID);
        objStub._getServiceClient().getOptions().setManageSession(true);

objConfigSetting = new DCAServiceConfigSetting();
nSocketTimeout = 30000;
objStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.SO_TIMEOUT, new Integer(nSocketTimeout));

//Setting the authentication for web service   

setServiceAuthentication(objStub, objConnSetting.getAuthenticationInfo());

//Setting Proxy properties

if(objConnSetting.getProxySettingStatus() == true)
{
   setProxyProperties(objStub, objConnSetting.getProxySettingInfo());
}

if(objConnSetting.getWebServiceURL().contains("https://"))
{
    if(objConnSetting.getWithoutSSLSerCertificateStatus() == true)
    {
      Protocol objProtocol = new Protocol("https", new MySocketFactory(), 443);
        objStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CUSTOM_PROTOCOL_HANDLER, objProtocol);
}



// command to set proxy setting
objStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.PROXY, objProxyProperties);


// Inside MySocketFactory class ----------------------------------------------

public Socket createSocket(final String host,
                           final int port,
                           final InetAddress localAddress,
                           final int localPort,
                           final HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException 

{
if (params == null){
throw new IllegalArgumentException("Parameters may not be null");
}
 int timeout = params.getConnectionTimeout();
 SocketFactory socketfactory = getSSLContext().getSocketFactory();
 if (timeout == 0) 
 {
        return socketfactory.createSocket(host, port, localAddress, localPort);
    }
    else 
    {
        Socket socket = socketfactory.createSocket();
        SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
        SocketAddress remoteaddr = new InetSocketAddress(host, port);
        socket.bind(localaddr);
        socket.connect(remoteaddr, timeout);
        return socket;
    }
}


   // Also 

 private static SSLContext createEasySSLContext() 
{
    try 
    {
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, new TrustManager[] {new NaiveTrustManager()}, null);
        return context;
    }
    catch (Exception e) 
    {
        LOG.error(e.getMessage(), e);
        throw new HttpClientError(e.toString());
    }
}
