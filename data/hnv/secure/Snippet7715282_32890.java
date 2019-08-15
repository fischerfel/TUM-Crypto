    public class ActivateHttpClient extends DefaultHttpClient { 
    final Context context;


    /**
     * Public constructor taking two arguments for ActivateHttpClient.
     * @param context - Context referencing the calling Activity, for creation of
     * the socket factory.
     * @param params - HttpParams passed to this, specifically to set timeouts on the
     * connection.
     */
    public ActivateHttpClient(Context context, HttpParams params) {
        this.setParams(params);
    }


    /* (non-Javadoc)
     * @see org.apache.http.impl.client.DefaultHttpClient#createClientConnectionManager()
     * Create references for both http and https schemes, allowing us to attach our custom
     * SSLSocketFactory to either
     */
    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        registry.register(new Scheme("https", newSslSocketFactory(), 443));
        return new SingleClientConnManager(getParams(), registry);
    }

    /**
     * Creation of new SSLSocketFactory, which imports a certificate from
     * a server which self-signs its own certificate.
     * @return
     */
    protected SSLSocketFactory newSslSocketFactory() {
        try {

            //Keystore must be in BKS (Bouncy Castle Keystore)
            KeyStore trusted = KeyStore.getInstance("BKS");

            //Reference to the Keystore
            InputStream in = context.getResources().openRawResource(
                    R.raw.cert);

            //Password to the keystore
            try {
                trusted.load(in, PASSWORD_HERE.toCharArray());
            } finally {
                in.close();
            }

            // Pass the keystore to the SSLSocketFactory. The factory is
            // responsible
            // for the verification of the server certificate.
            SSLSocketFactory sf = new SSLSocketFactory(trusted);

            // Hostname verification from certificate
            // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
            sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            return sf;

            // return new SSLSocketFactory(trusted);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

}
