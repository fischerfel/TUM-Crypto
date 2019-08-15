    public class Register  extends Activity {


EditText inputUsername;
EditText inputNama;
EditText inputEmail;
EditText inputPassword;
EditText inputPassword2;
Button btnRegister;
Button btnLogin;


private ProgressDialog pDialog; 

JSONParser jsonParser = new JSONParser();

private static final String LOGIN_URL = "http://10.0.3.2/1aaa/register.php";

private static final String TAG_SUCCESS = "success";
private static final String TAG_MESSAGE = "message";

@Override
public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.register);

    inputUsername = (EditText) findViewById(R.id.username);
    inputNama = (EditText) findViewById(R.id.name);
    inputEmail = (EditText) findViewById(R.id.email);
    inputPassword = (EditText) findViewById(R.id.password);
    inputPassword2 = (EditText) findViewById(R.id.password2);


    btnRegister = (Button) findViewById(R.id.btnRegister);

    btnLogin = (Button) findViewById(R.id.btnLinkToLogin);


}



    public void btnRegister(View v) {
        // TODO Auto-generated method stub

        Log.e("ini", "bisa");
        new CreateUser().execute();
        Log.e("ini", "bisa");

    }


    class CreateUser extends AsyncTask<String, String, String> {

         /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @SuppressWarnings("static-access")
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
             // Check for success tag
            int success;

                String username = inputUsername.getText().toString();
                String name = inputNama.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String password2 = inputPassword2.getText().toString();
                Encrypt a = new Encrypt();
                a.encrrypte(password);
                String encpassword = a.GetHashtext();
                a.encrrypte(password2);
                String encretypepwd = a.GetHashtext();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String output = formatter.format(c);

                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("username", username));
                        params.add(new BasicNameValuePair("name", name));
                        params.add(new BasicNameValuePair("email", email));
                        params.add(new BasicNameValuePair("password", encpassword));
                        params.add(new BasicNameValuePair("password2", encretypepwd));

                        params.add(new BasicNameValuePair("ctime",  output));
                        params.add(new BasicNameValuePair("date_edit_profile", "0000-00-00 00:00:00"));


                        Log.d("request!", "starting");

                        //Posting user data to script 
                        JSONObject json = jsonParser.makeHttpRequest(
                               LOGIN_URL, "POST", params);

                        // full json response
                        Log.d("Login attempt", json.toString());

                        // json success element
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            Log.d("User Created!", json.toString());                
                            finish();
                            return json.getString(TAG_MESSAGE);
                        }else{
                            Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                            return json.getString(TAG_MESSAGE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return null;

                }
                /**
                 * After completing background task Dismiss the progress dialog
                 * **/


                    protected void onPostExecute(String file_url) {
                    // dismiss the dialog once product deleted
                    pDialog.dismiss();
                    if (file_url != null){
                        Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
                    }

                }
    }

    public void btnLogin(View v){
        Intent s = new Intent(this, Login.class);
        startActivity(s);
    }



            public class Encrypt{
                private String hashtext= "";
                public void encrrypte(String yourString){
                    try {
                        byte[] bytesOfMessage = yourString.getBytes("UTF-8");
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        byte[] thedigest = md.digest(bytesOfMessage);
                        BigInteger bigInt = new BigInteger(1,thedigest);
                        hashtext = bigInt.toString(16);
                        // Now we need to zero pad it if you actually want the full 32 chars.
                        while(hashtext.length() < 32 ){
                          hashtext = "0"+hashtext;
                        }
                    } catch (UnsupportedEncodingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                public String GetHashtext(){
                    return hashtext;
                }

            }
    }
