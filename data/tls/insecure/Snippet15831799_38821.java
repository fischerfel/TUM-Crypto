javax.net.ssl.SSLContext ctx = javax.net.ssl.SSLContext.getInstance("TLS");
ctx.init(keyfactory.getKeyManagers(), tmfactory.getTrustManagers(), new    SecureRandom());
..
SchemeRegistry schemeRegistry = new SchemeRegistry();
schemeRegistry.register(new Scheme("https", 443, new MySSLFactory(ctx))); 
PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
cm.setMaxTotal(20);
cm.setDefaultMaxPerRoute(5);
cm.setMaxPerRoute(new HttpRoute(localhost), 5);
HttpParams httpParameters = new BasicHttpParams();
HttpConnectionParams.setConnectionTimeout(httpParameters, 5000); // Connection timeout
HttpConnectionParams.setSoTimeout(httpParameters, 0);
HttpClient client = new DefaultHttpClient(cm,httpParameters);           
while (i<200) {
    HttpPost post = new HttpPost(send.getUrl().toString());
    Header[] heads =  post.getAllHeaders();
    System.out.println("Request Headers size:"+heads.length);
    for (int ii=0; ii<heads.length; ii++) {
        Header head = heads[ii];
        System.out.println("Name:"+head.getName()+",  value:"+head.getValue()); 
    }       
    ContentType contentType =     ContentType.create("text/xml","UTF-8");       
    HttpEntity entity = new StringEntity(data,contentType );
    post.setEntity(entity);
    HttpResponse resp = client.execute(post);
...
}
client.getConnectionManager().shutdown();
