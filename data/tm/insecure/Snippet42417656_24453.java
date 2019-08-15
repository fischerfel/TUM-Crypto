TrustManager[] trustAllCerts = new TrustManager[]{
    new X509TrustManager(){
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
    return null;
}

public void checkClientTrusted(X509Certificate[] certs, String authType) {}
public void checkServerTrusted(X509Certificate[] certs, String authType) {}
}
};
SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null, trustAllCerts, new SecureRandom());
HttpHost proxy = new HttpHost(proxyHostName, Integer.parseInt(proxyPort));
DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSslcontext(sc).setRoutePlanner(routePlanner).build();

Executor executor = Executor.newInstance(httpClient)
    .auth(proxyUserName, proxyPassword);

log.debug("ABOUT TO EXECUTE A URL");
String str = executor.execute(Request.Get("https://www.google.com/")).returnContent().asString();
log.debug("####" + str + "@@@@");
