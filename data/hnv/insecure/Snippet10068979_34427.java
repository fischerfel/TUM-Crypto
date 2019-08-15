public class HttpsClient extends DefaultHttpClient {
private final Context context;

public HttpsClient(final Context context) {
    super();
    this.context = context;
}

/**
 * The method used to create client connection manager
 */
@Override
protected ClientConnectionManager createClientConnectionManager() {
    final SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));

    // Register for port 443 our SSLSocketFactory with our keystore
    // to the ConnectionManager
    registry.register(new Scheme("https", newSslSocketFactory(), 8443));
    return new SingleClientConnManager(getParams(), registry);
}

private SSLSocketFactory newSslSocketFactory() {
    try {
        // Get an instance of the Bouncy Castle KeyStore format
        final KeyStore trusted = KeyStore.getInstance("BKS");
        // Get the raw resource, which contains the keystore with
        // your trusted certificates (root and any intermediate certs)
        final InputStream inputStream = context.getResources().openRawResource(R.raw.parkgroup_ws_client);
        try {
            // Initialize the keystore with the provided truste
            // certificates
            // Also provide the password of the keystore
            trusted.load(inputStream, "myKeyStorePassword".toCharArray());
        } finally {
            inputStream.close();
        }
        // Pass the keystore to the SSLSocketFactory. The factory is
        // responsible
        // for the verification of the server certificate.
        final SSLSocketFactory ssf = new SSLSocketFactory(trusted);
        // Hostname verification from certificate
        // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return ssf;
    } catch (Exception e) {
        Log.e("MYTAG", e.getMessage());
        throw new AssertionError(e);
    }
}

@Override
protected HttpParams createHttpParams() {
    final HttpParams httpParams = super.createHttpParams();
    httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);
    httpParams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
    return httpParams;
}
