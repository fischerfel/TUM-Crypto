public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();
    public static final int PORT = 4646;
    public static final String HOST = "192.168.1.12"; //Change to IP address of server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect();
    }

    private void connect() {
        new SocketConnector(this).execute();
    }

    static class SocketConnector extends AsyncTask<Void, Void, String> {
        Activity activity;
        TrustManagerFactory tmf;
        KeyManagerFactory kmf;
        KeyStore publicKeyStore;
        KeyStore privateKeyStore;
        SSLSocket socket;

        public SocketConnector(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected String doInBackground(Void... params) {
            InputStream privateKeyStoreIns;
            InputStream publicKeyStoreIns;

            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextInt();

            privateKeyStoreIns = activity.getResources().openRawResource(R.raw.client_private);
            publicKeyStoreIns = activity.getResources().openRawResource(R.raw.server_public);

            try {
                privateKeyStore = setupKeystore(privateKeyStoreIns, "private");
                publicKeyStore = setupKeystore(publicKeyStoreIns, "public");

                tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(publicKeyStore);

                kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(privateKeyStore, "private".toCharArray());

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(kmf.getKeyManagers(),
                        tmf.getTrustManagers(),
                        secureRandom);

                SSLSocketFactory sf = sslContext.getSocketFactory();
                socket = (SSLSocket) sf.createSocket(HOST, PORT);
                socket.startHandshake();

            } catch (Exception e) {
                e.printStackTrace();
                return "Connection failure: " + e.getMessage();
            }

            return "Connection established!";
        }

        @Override
        protected void onPostExecute(String s) {
            TextView textView = (TextView) activity.findViewById(R.id.text_view);
            textView.setText(s);
        }

        private KeyStore setupKeystore(InputStream keyStoreInputStream, String passphrase)
                throws GeneralSecurityException, IOException {
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(keyStoreInputStream, passphrase.toCharArray());

            return keyStore;
        }
    }
}
