ClassLoader cl = HCTest.class.getClassLoader();
URL url = cl.getResource("test.keystore");
KeyStore keystore  = KeyStore.getInstance("jks");
char[] pwd = "nopassword".toCharArray();
keystore.load(url.openStream(), pwd);

TrustManagerFactory tmf = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keystore);
TrustManager[] tm = tmf.getTrustManagers();

KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
        KeyManagerFactory.getDefaultAlgorithm());
kmfactory.init(keystore, pwd);
KeyManager[] km = kmfactory.getKeyManagers();

SSLContext sslcontext = SSLContext.getInstance("TLS");
sslcontext.init(km, tm, null);

LocalTestServer localServer = new LocalTestServer(sslcontext);
localServer.registerDefaultHandlers();

localServer.start();
try {

    DefaultHttpClient httpclient = new DefaultHttpClient();
    TrustStrategy trustStrategy = new TrustStrategy() {

        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            for (X509Certificate cert: chain) {
                System.err.println(cert);
            }
            return false;
        }

    };

    SSLSocketFactory sslsf = new SSLSocketFactory("TLS", null, null, keystore, null,
            trustStrategy, new AllowAllHostnameVerifier());
    Scheme https = new Scheme("https", 443, sslsf);
    httpclient.getConnectionManager().getSchemeRegistry().register(https);

    InetSocketAddress address = localServer.getServiceAddress();
    HttpHost target1 = new HttpHost(address.getHostName(), address.getPort(), "https");
    HttpGet httpget1 = new HttpGet("/random/100");
    HttpResponse response1 = httpclient.execute(target1, httpget1);
    System.err.println(response1.getStatusLine());
    HttpEntity entity1 = response1.getEntity();
    EntityUtils.consume(entity1);
    HttpHost target2 = new HttpHost("www.verisign.com", 443, "https");
    HttpGet httpget2 = new HttpGet("/");
    HttpResponse response2 = httpclient.execute(target2, httpget2);
    System.err.println(response2.getStatusLine());
    HttpEntity entity2 = response2.getEntity();
    EntityUtils.consume(entity2);
} finally {
    localServer.stop();
}
