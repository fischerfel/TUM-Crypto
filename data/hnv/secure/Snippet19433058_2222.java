public class MainActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // allows network on main thread (temp hack)
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
    StrictMode.setThreadPolicy(policy);

    SchemeRegistry schemeRegistry = new SchemeRegistry();
    //schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
    schemeRegistry.register(new Scheme("https", newSSLSocketFactory(), 443));


    HttpParams params = new BasicHttpParams();

    SingleClientConnManager mgr = new SingleClientConnManager(params, schemeRegistry);

    HttpClient client = new DefaultHttpClient(mgr, params);

    HttpPost httpRequest = new HttpPost("https://our-web-service.com");

    try {
        client.execute(httpRequest);
    } catch (Exception e) {
        e.printStackTrace(); //
    }
}

/* 
 * Standard SSL CA Store Setup //
 */
private SSLSocketFactory newSSLSocketFactory() {

    KeyStore trusted;

    try {
        trusted = KeyStore.getInstance("AndroidCAStore");
        trusted.load(null, null);
        Enumeration<String> aliases = trusted.aliases();

        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            X509Certificate cert = (X509Certificate) trusted.getCertificate(alias);
            Log.d("", "Alias="+alias);
            Log.d("", "Subject DN: " + cert.getSubjectDN().getName());
            Log.d("", "Issuer DN: " + cert.getIssuerDN().getName());
        }      

        SSLSocketFactory sf = new SSLSocketFactory(trusted);
        sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

        return sf;

    } catch (Exception e) {
        // TODO Auto-generated catch block
        throw new AssertionError(e);
    }  
}

}
