DefaultHttpClient httpClient = new DefaultHttpClient();

try {
    SSLContext ctx = SSLContext.getInstance("TLSv1.1");
    TrustManager[] trustManagers = getTrustManagers("jks", new FileInputStream(new File("C:\\SSLKeyStore.ks")), "changeit");
    ctx.init(null, trustManagers, new SecureRandom());

    HttpGet httpget = new HttpGet("https://localhost:8844/Channels/HTTP/getData");
    System.out.println("executing request" + httpget.getRequestLine());

    SSLSocketFactory factory = new SSLSocketFactory(ctx);
    factory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    ClientConnectionManager manager = httpClient.getConnectionManager();
    manager.getSchemeRegistry().register(new Scheme("https", 443, factory));
    manager.getSchemeRegistry().register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

    HttpHost proxy = new HttpHost("localhost", 5555, "http");
    httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

    HttpResponse response = httpClient.execute(httpget);
    HttpEntity entity = response.getEntity();

    System.out.println("----------------------------------------");
    System.out.println(response.getStatusLine());
    if (entity != null) {
        System.out.println("Response content length: " + entity.getContentLength());
    }
    EntityUtils.consume(entity);
} catch (Exception exception) {
    exception.printStackTrace();
} finally {
    httpClient.getConnectionManager().shutdown();
}
