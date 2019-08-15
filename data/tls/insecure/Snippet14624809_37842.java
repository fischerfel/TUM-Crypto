public class LoginActivity extends Activity {

    Context context;
    int flagForKeepLoggedIn = 1;
    private String TAG = "Vik";
    private String username;
    private String password;
    private static final String SOAP_ACTION = "http://xyz.org/gettemp";
    private static final String OPERATION_NAME = "gettemp";
    private static final String WSDL_TARGET_NAMESPACE = "http://xyz.org/";
    private static final String SOAP_ADDRESS = "https://12.12.12.11:3636/anonymous/gettemp/uyh.asmx";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context = this;
        final DataBaseHandler db = new DataBaseHandler(this);

        final EditText usernameValueEditText = (EditText) findViewById(R.id.username_value);
        final EditText passwordValueEditText = (EditText) findViewById(R.id.password_value);
        final CheckBox keepMeLoggedInCheckBox = (CheckBox) findViewById(R.id.checkbox_keep_me_logged_in);

        keepMeLoggedInCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (db.ifTableExists()) {
                    if (isChecked) {
                        // db.updateKeepLoggedInFlag("1");
                        flagForKeepLoggedIn = 1;
                    } else {
                        // db.updateKeepLoggedInFlag("0");
                        flagForKeepLoggedIn = 0;
                    }
                }
            }
        });

        if (db.ifTableExists()) {
            if ((db.getKeepLoggedInFlagValue()).equals("1")) {
                List<User> userList = db.getStudentDetails();
                for (User cn1 : userList) {
                    usernameValueEditText.setText(cn1.getUserName());
                    passwordValueEditText.setText(cn1.getPassword());
                }
            }
        }

        TextView textViewForgotPassword = (TextView) findViewById(R.id.textview_forgot_password);
        final Button loginButton = (Button) findViewById(R.id.button_login);

        // Login Button Clicked
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameValueEditText.getText().toString();
                password = passwordValueEditText.getText().toString();

                if(validate()) {
                    db.createTables();
                    db.addInfoDetails(flagForKeepLoggedIn);

                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(loginButton.getWindowToken(), 0);
                    Toast.makeText(context, "Username or Password is empty",
                    Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean validate() {
        return !"".equals(username.trim()) && !"".equals(password.trim());
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Log.i(TAG, "doInBackground");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            CallWebServiceForAuthentication();
        }

        @Override
        protected void onPreExecute() {
            // Log.i(TAG, "onPreExecute");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // Log.i(TAG, "onProgressUpdate");
        }

        public void CallWebServiceForAuthentication() {
            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

            request.addProperty("userName", username);
            request.addProperty("pwd", password);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            allowAllSSL();

            HttpsTransportSE aht = new KeepAliveHttpsTransportSE("12.12.12.11:3636", 443, "anonymous/gettemp/uyh.asmx", 1000);

            //HttpsTransportSE aht = new KeepAliveHttpsTransportSE("12.12.12.11:3636", 443, "anonymous/gettemp/uyh.asmx", 1000);
            //HttpTransportSE aht = new HttpTransportSE(SOAP_ADDRESS);

            try {
                aht.call(SOAP_ACTION, envelope);
                SoapObject response = (SoapObject) envelope.bodyIn;
                SOAPResponseManager soapManager = new SOAPResponseManager(this);
            } catch (Exception e) {
                aht.reset();
                e.printStackTrace();    
            }
        }

    private static TrustManager[] trustManagers;

    public static class _FakeX509TrustManager implements javax.net.ssl.X509TrustManager {
        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        public boolean isClientTrusted(X509Certificate[] chain) {
            return (true);
        }

        public boolean isServerTrusted(X509Certificate[] chain) {
            return (true);
        }

        public X509Certificate[] getAcceptedIssuers() {
            return (_AcceptedIssuers);
        }
    }

    public static void allowAllSSL() {
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    });

    javax.net.ssl.SSLContext context = null;

    if (trustManagers == null) {
        trustManagers = new javax.net.ssl.TrustManager[] { new _FakeX509TrustManager() };
    }

    try {
        context = javax.net.ssl.SSLContext.getInstance("TLS");
        context.init(null, trustManagers, new SecureRandom());
    } catch (NoSuchAlgorithmException e) {
        Log.e("allowAllSSL", e.toString());
    } catch (KeyManagementException e) {
        Log.e("allowAllSSL", e.toString());
    }
    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
}
