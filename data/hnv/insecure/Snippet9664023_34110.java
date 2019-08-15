HttpClient httpClient = null;
try
{

    HttpParams httpParameters = new BasicHttpParams();

    KeyStore rootca = KeyStore.getInstance(KeyStore.getDefaultType());
    rootca.load(new FileInputStream("server.jks"), "bara".toCharArray());

    KeyStore mycert = KeyStore.getInstance("JKS");
    mycert.load(new FileInputStream("client.jks"), "bara".toCharArray());

    SSLSocketFactory sockfact = new SSLSocketFactory(mycert, "bara", rootca);
    sockfact.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    registry.register(new Scheme("https", sockfact, 443));
    httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParameters, registry), httpParameters);

    HttpGet get = new HttpGet("https://mycomputer.mynetwork/test");
    HttpResponse response = httpClient.execute(get);

    System.out.println(response.getStatusLine());

}
