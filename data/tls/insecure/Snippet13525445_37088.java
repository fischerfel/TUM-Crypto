public class ClientCertificateActivity extends Activity implements
    KeyChainAliasCallback {

protected static final String TAG = "CERT_TEST";
private String alias;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    choseCertificate();
    LinearLayout layout = new LinearLayout(this);
    Button connectToServer = new Button(this);
    connectToServer.setText("Try to connect to Server");
    connectToServer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    connectToServer.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            connectToServer();
        }
    });
    layout.addView(connectToServer);
    addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
}

protected void connectToServer() {
    final Context ctx = this;
    new AsyncTask<Void, Void, Boolean>() {

        private Exception error;

        @Override
        protected Boolean doInBackground(Void... arg) {
            try {
                PrivateKey pk = KeyChain.getPrivateKey(ctx, alias);
                X509Certificate[] chain = KeyChain.getCertificateChain(ctx,
                        alias);

                KeyStore keyStore = KeyStore.getInstance("AndroidCAStore");
                TrustManagerFactory tmf = TrustManagerFactory
                        .getInstance(TrustManagerFactory
                                .getDefaultAlgorithm());
                tmf.init(keyStore);

                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);

                URL url = new URL("https://usecert.example.com/");
                HttpsURLConnection urlConnection = (HttpsURLConnection) url
                        .openConnection();
                urlConnection.setSSLSocketFactory(context
                        .getSocketFactory());
                InputStream in = urlConnection.getInputStream();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                error = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean valid) {
            if (error != null) {
                Toast.makeText(ctx, "Error: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(ctx, "Success: ", Toast.LENGTH_SHORT).show();
        }
    }.execute();

}

protected void choseCertificate() {
    KeyChain.choosePrivateKeyAlias(this, this,
            new String[] { "RSA", "DSA" }, null, "m.ergon.ch", 443, null);
}

@Override
public void alias(String alias) {
    this.alias = alias;
}
}
