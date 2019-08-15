public class CertificatePinning {

  static SSLSocketFactory constructSSLSocketFactory(Context context) {

    SSLSocketFactory sslSocketFactory = null;

    try {
        AssetManager assetManager = context.getAssets();
        InputStream keyStoreInputStream = assetManager.open("myapp.store");
        KeyStore trustStore = KeyStore.getInstance("BKS");

        trustStore.load(keyStoreInputStream, "somepass".toCharArray());

        sslSocketFactory = new SSLSocketFactory(trustStore);
        sslSocketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
    }
    catch(Exception e){

        Log.d("Exception", e.getLocalizedMessage());
    }

    return sslSocketFactory;
}

public static HttpClient getNewHttpClient(Context context) {

    DefaultHttpClient httpClient = null;

    try {

        SSLSocketFactory sslSocketFactory = constructSSLSocketFactory(context);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sslSocketFactory, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

        httpClient = new DefaultHttpClient(ccm, params);

    } catch (Exception e) {

        Log.d("Exception", e.getLocalizedMessage() );

        return null;
    }

    return httpClient;
}
