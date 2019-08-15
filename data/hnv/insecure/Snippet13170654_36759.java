public class SecureHttpClient extends DefaultHttpClient {
    private String ks = "11111";
    private String ts = "22222";
    private Context context;

    public SecureHttpClient(final Context context) {
        super();
        this.context = context;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory factory = newSslSocketFactory();
        factory.setHostnameVerifier((X509HostnameVerifier) SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        registry.register(new Scheme("https", factory, 443));
        HttpParams httpParams = new BasicHttpParams();
        return new SingleClientConnManager(httpParams, registry);
    }

    private SSLSocketFactory newSslSocketFactory() {
        try {
            KeyStore keyStore = KeyStore.getInstance("BKS");
            KeyStore trustStore = KeyStore.getInstance("BKS");
            InputStream inKey= context.getResources().openRawResource(R.raw.client);
            InputStream inTrust = context.getResources().openRawResource(R.raw.clienttruststore);
            try {
                keyStore.load(inKey, ks.toCharArray());
                trustStore.load(inTrust, ts.toCharArray());
            } finally {
                inKey.close();
                inTrust.close();
            }
            return new SSLSocketFactory(trustStore);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
