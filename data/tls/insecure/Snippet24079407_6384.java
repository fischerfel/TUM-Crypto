private PaymentService_Service service = null;
private PaymentService iface = null;

@Before
public void setUp() throws Exception {
    System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
    System.setProperty("javax.net.debug", "ssl");
    service = new PaymentService_Service();
    iface = service.getPaymentServiceImplPort();
    Client client = ClientProxy.getClient(iface);
    HTTPConduit http = (HTTPConduit) client.getConduit();
    TLSClientParameters parameters = new TLSClientParameters();
    parameters.setSSLSocketFactory(createSSLContext().getSocketFactory());
    http.setTlsClientParameters(parameters);
    HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
    httpClientPolicy.setConnectionTimeout(36000);
    httpClientPolicy.setAllowChunking(false);
    httpClientPolicy.setReceiveTimeout(32000);
    http.setClient(httpClientPolicy);
}

private SSLContext createSSLContext() throws Exception{
    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    trustStore.load(new FileInputStream("/home/user/dev/project/key/http.jks"), "changeit".toCharArray());

    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(new FileInputStream("/home/user/dev/project/key/client.jks"), "changeit".toCharArray());


    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(trustStore);

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(keyStore, "changeit".toCharArray());

    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(kmf.getKeyManagers() , tmf.getTrustManagers(), new SecureRandom());
    return sslContext;
}

@Test
public void testSomeMethod() throws Exception {
    Client client = ClientProxy.getClient(iface);
    client.getInInterceptors().add(new LoggingInInterceptor());
    client.getOutInterceptors().add(new LoggingOutInterceptor());
    String res = iface.doSomeMethod();
}
