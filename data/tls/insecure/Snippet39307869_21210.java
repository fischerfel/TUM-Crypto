public class MainActivity extends ListActivity  implements Callback<TitleReleases> {

    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Certificate ca =  null;
        OkHttpClient okHttpClient = new OkHttpClient();

        try {
            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = getApplicationContext().getResources().openRawResource(R.raw.stargate);
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }
            } catch (Exception ex) {
                Log.e("MEH", "Unable to load the certificate: " + ex.getMessage());
        }

        // SSLContext sslContext = null;
        try {
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // creating a TrustManager that trusts the CAs in our keyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // creating an SSLSocketFactory that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init( null, tmf.getTrustManagers(), null );
            /* okhttp version 3 way:
            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .build();
            */
            // okhttp version 2 way:
            okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
        } catch ( Exception ex ) {
            Log.e("MEH", "Unable to create the trust manager: " + ex.getMessage() );
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://www.mehconsulting.com:8443")
                // .setClient( new OkClient( okHttpClient ) )
                .setClient(  new OkClient( okHttpClient )  )
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        MEHServiceInterface mehService = restAdapter.create( MEHServiceInterface.class );

        mehService.getReleases(this);
    }

    @Override
    public void failure( RetrofitError exception ) {
        Toast.makeText( this, exception.getMessage(), Toast.LENGTH_LONG).show();
        Log.e(getClass().getSimpleName(), "Exception from Retrofit request to MEHService", exception );
    }

    @Override
    public void success( TitleReleases releases, Response response ) {
        setListAdapter( new TitleReleasesAdapter( releases.titleReleases ) );
    }

    class TitleReleasesAdapter extends ArrayAdapter<TitleRelease> {
        TitleReleasesAdapter( List<TitleRelease> titleReleases ) {
            super(MainActivity.this, android.R.layout.simple_list_item_1, titleReleases );
        }
    }

}
