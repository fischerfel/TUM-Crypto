 public class DefaultHttpRequestHandler implements HttpRequestHandler {

private static String sslType = "TLS";
private HttpClient httpClient = null;
private ThreadSafeClientConnManager tm;
public DefaultHttpRequestHandler() {
    super();
    init();

}

private void init() {
    try {
        SSLContext sslCtx = SSLContext.getInstance(sslType);

    //  sslCtx.init(null,new TrustManager[] { new EasyX509TrustManager() }, null);


        sslCtx.init(null, new TrustManager[] { new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            System.out.println("getAcceptedIssuers =============");
                            return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs,
                                    String authType) {
                            System.out.println("checkClientTrusted =============");
                    }

                    public void checkServerTrusted(X509Certificate[] certs,
                                    String authType) {
                            System.out.println("checkServerTrusted =============");
                    }

                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] arg0,
                            String arg1) throws CertificateException {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] arg0,
                            String arg1) throws CertificateException {
                        // TODO Auto-generated method stub

                    }
        } }, new SecureRandom());

    SSLSocketFactory sf = new SSLSocketFactory(sslCtx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


        Scheme https = new Scheme("https", 443, sf);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(https);

        Scheme http = new Scheme("http", 80, PlainSocketFactory.getSocketFactory());
        schemeRegistry.register(http);

        tm = new ThreadSafeClientConnManager(schemeRegistry);
        //httpClient = new  ContentEncodingHttpClient(tm);
        httpClient = new DefaultHttpClient(tm);
        httpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true); 
        //httpClient.getConnectionManager().getSchemeRegistry()         .register(https);

    } catch (Exception e) {
        System.err.println(e.getMessage());
        e.printStackTrace();
    }

}

public void handle(HttpRequest request, HttpResponse response,
        HttpContext context) throws HttpException, IOException {

    System.out.println(request);
    RequestLine reqLine = request.getRequestLine();
    if(reqLine.getMethod().equalsIgnoreCase("CONNECT"))
    {

        response.setEntity(new BufferedHttpEntity(new StringEntity("HTTP/1.0 200 Connection established\r\nProxy-agent: proxy client\r\n\r\n")));
        //do i switch the socket to sslsocketconnection in defaulthttpserver here?
    }
    else
    {
        try {

            HttpResponse clientResponse = null;

            HttpEntity entity = null;

            clientResponse = httpClient.execute(new RequestWrapper(request));

            entity = clientResponse.getEntity();

            if (entity != null) {
                response.setEntity(new BufferedHttpEntity(entity));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } 

    }

}
