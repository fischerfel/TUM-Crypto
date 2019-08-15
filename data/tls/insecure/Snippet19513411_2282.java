public class POPLogin extends Activity {

private EditText usr, pass;
private Button submitButton;
private CharSequence notify;
private String res, resp;
private final String link = "https://pop-portal.tut.fi/portal/page/portal/POP-portaali/20Opinnot/22Omat%20suoritukset";
private URL url;

public static boolean isNetworkAvailable(Context context) {
    return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
}

protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_poplogin);
    usr = ((EditText) findViewById(R.id.usernameField));            
    pass = ((EditText) findViewById(R.id.passwordField));           
    submitButton = (Button) findViewById(R.id.submitButton);        
    submitButton.getBackground().setColorFilter(new LightingColorFilter(0x00FFB90F, 0xFFAA0000));   

    /*
     * This method controls the login button so that the correct
     * information is sent to the server.
     */
    submitButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            if(isNetworkAvailable(getApplicationContext())) {   // If there is an Internet connection available

                /*
                * A new thread is created from the main one 
                * to separate the login process (AsyncTask, https operations).
                */
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            url = new URL(link);
                            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                            // Create the SSL Connection
                            SSLContext sc;
                            sc = SSLContext.getInstance("TLS");
                            sc.init(null, null, new java.security.SecureRandom());
                            conn.setSSLSocketFactory(sc.getSocketFactory());

                            // SSL Authentication
                            String userpass = usr.getText().toString() + ":" + pass.getText().toString();
                            String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
                            conn.setRequestProperty("Authorization", basicAuth);

                            Log.i("NOTIFICATION", "All SSL parameters set");

                            // Set timeout and method
                            conn.setReadTimeout(7000);
                            conn.setConnectTimeout(7000);
                            conn.setRequestMethod("POST");
                            conn.setDoInput(true);          // Flag indicating this connection is used for output (POST)
                            conn.connect();

                            Log.i("NOTIFICATION", "Connection request sent");

                            // Check HTTP Response Code
                            int status = conn.getResponseCode();
                            InputStream is;
                            if (status >= 400 ) {
                                is = conn.getErrorStream();
                            } else {
                                is = conn.getInputStream();
                            }

                            Log.i("NOTIFICATION", "HTTP Code Checked");

                            // Receives the answer
                            resp = null;
                            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                            while((res = rd.readLine()) != null) {
                                resp += res;
                            }
                            Log.i("NOTIFICATION", "Answer received");
                            Log.i("CODE", resp);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                notify = "Internet Connection not available";
            }

            if(notify != null) {
                Toast toast = Toast.makeText(getApplicationContext(), notify, Toast.LENGTH_SHORT);
                toast.show();
                notify = null;
            } 
        }
    });
}

public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.poplogin, menu);
    return true;
}
