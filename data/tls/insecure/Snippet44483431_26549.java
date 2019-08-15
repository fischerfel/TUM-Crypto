public class VerifyKey extends AsyncTask<Void, Void, Void> {

    public static final String CERTIFICATE_TYPE_X_509 = "X.509";
    public static final String CERTIFICATE_ALIAS = "domain_name";
    public static final String SERVER_URL = "https://domain_name";

    Context mContext;

    public VerifyKey(Context c)
    {
        mContext=c;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try
        {
            // Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
            InputStream caInput = mContext.getResources().openRawResource(R.raw.ca);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                Log.d("ca=" + ((X509Certificate) ca).getSubjectDN(),"CONN_TEST");

            } finally {
                caInput.close();
            }

// Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            MainActivity.tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            MainActivity.tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
            MainActivity.ssl_context = SSLContext.getInstance("TLS");
            MainActivity.ssl_context.init(null, MainActivity.tmf.getTrustManagers(), null);

// Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url = new URL("https://domain_name/register.php");
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection)url.openConnection();
            urlConnection.setSSLSocketFactory(MainActivity.ssl_context.getSocketFactory());
            InputStream in = urlConnection.getInputStream();

            BufferedReader br=new BufferedReader(new InputStreamReader(in));

            Log.d(br.readLine(),"CONN_TEST_SSL");
        }
        catch(Exception ex)
        {
            Log.d(ex.getMessage(),"CONN_TEST_SSL");
        }

        return null;
    }
}
