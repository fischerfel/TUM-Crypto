/* Load the keyStore that includes self-signed cert as a "trusted" entry. */
KeyStore keyStore = KeyStore.getInstance("JKS");
keyStore.load(new FileInputStream("myjks.jks"), "123456".toCharArray());
TrustManagerFactory tmf = 
    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keyStore);
SSLContext ctx = SSLContext.getInstance("TLSv1");
ctx.init(null, tmf.getTrustManagers(), null);
SSLSocketFactory sslFactory = ctx.getSocketFactory();

HttpClientBuilder builder = HttpClientBuilder.create();
SSLConnectionSocketFactory sslConnectionFactory = 
    new SSLConnectionSocketFactory(ctx, 
        SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
builder.setSSLSocketFactory(sslConnectionFactory);

Registry<ConnectionSocketFactory> registry = 
    RegistryBuilder.<ConnectionSocketFactory>create()
        .register("https", sslConnectionFactory)
        .build();

HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);

builder.setConnectionManager(ccm);
CloseableHttpClient client = builder.build();

HttpPost post = new HttpPost("https://myurl.com:9999/post");

/* post has parameters - omitted */

HttpResponse response = client.execute(post);
HttpEntity entity = response.getEntity();
String responseString = EntityUtils.toString(entity, "UTF-8");
int httpCode = response.getStatusLine().getStatusCode();
System.out.println(responseString);
System.out.println(httpCode);
