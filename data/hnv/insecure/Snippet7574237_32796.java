public class HttpC2Connection {

public static HttpEntity CreateHttpEntityFromP12(String uri,
        String keyFilePath, String keyPass) throws Exception {

    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    keyStore.load(new FileInputStream(keyFilePath), keyPass.toCharArray());

    SSLSocketFactory sf = new MySSLSocketFactory(keyStore);
    sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

    SchemeRegistry registry = new SchemeRegistry();

    registry.register(new Scheme("https", sf, 443));

    ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
            registry);

    HttpClient httpclient = new DefaultHttpClient(ccm, params);
    HttpGet httpget = new HttpGet(uri);
    HttpResponse response = httpclient.execute(httpget);
    HttpEntity entity = response.getEntity();

    return entity;
}
