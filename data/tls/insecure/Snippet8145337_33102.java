 public class DefaultHttpServer {

public static void main(String[] args) throws Exception {

    Thread t = new RequestListenerThread(8080);
    t.setDaemon(false);
    t.start();

    //send a request to proxy server for testing
    testSendReqFromClient() ;
}

public static void testSendReqFromClient() throws Exception
{

    SSLContext sslCtx = SSLContext.getInstance("TLS");

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

    Thread.sleep(5000);
    SSLSocketFactory sf = new SSLSocketFactory(sslCtx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    Scheme https = new Scheme("https", 443, sf);
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(https);
    Scheme http = new Scheme("http", 80, PlainSocketFactory.getSocketFactory());
    schemeRegistry.register(http);
    BasicHttpRequest req = new BasicHttpRequest("GET","https://www.yahoo.com");
    ThreadSafeClientConnManager tm = new ThreadSafeClientConnManager(schemeRegistry);
    HttpClient httpClient = new DefaultHttpClient(tm);
    ConnRouteParams.setDefaultProxy(req.getParams(), new HttpHost("localhost",8080,"http"));
    httpClient.execute(new RequestWrapper(req));
}

 }
