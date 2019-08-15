public class MyHttpClient extends DefaultHttpClient {

final Context context;

public MyHttpClient(Context context) {
    this.context = context;
}

@Override
protected ClientConnectionManager createClientConnectionManager() {

        KeyStore trustStore = null;
            trustStore = KeyStore.getInstance("BKS");

        InputStream in = context.getResources().openRawResource(R.raw.mykeystore);
        try {
            // Initialize the keystore with the provided trusted certificates
            // Also provide the password of the keystore
            trustStore.load(in, "root01".toCharArray());
        } 
        } finally {

                in.close();

        }

        SSLSocketFactory sf=null;

            sf = new MySSLSocketFactory(trustStore);

        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 9006));
    return new SingleClientConnManager(params, registry);
}
}
