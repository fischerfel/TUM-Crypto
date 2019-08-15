public class SampleActivity extends Activity implements OnClickListener {

    // Keep default context and factory
    private SSLContext mDefaultSslContext;
    private SSLSocketFactory mDefaultSslFactory;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.button_id).setOnClickListener(this);

        try {
            // Initialize context and factory
            mDefaultSslContext = SSLContext.getInstance("TLS");
            mDefaultSslContext.init(null, null, null);
            mDefaultSslFactory = mDefaultSslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    @Override
    public void onClick(View v){
        SSLContext sslcontext;
        SSLSocketFactory sslfactory;

        try {
            // If using this factory, enable Keep-Alive
            sslfactory = mDefaultSslFactory;

            // If using this factory, enable session resumption (abbreviated handshake)
            sslfactory = mDefaultSslContext.getSocketFactory();

            // If using this factory, enable full handshake each time
            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, null, null);
            sslfactory = sslcontext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        URL url = new URL("https://example.com");
        HttpsURLConnection = conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(sslfactory);
        conn.connect();
    }
}
