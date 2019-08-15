    package test.example.com.test;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v4.app.FragmentActivity;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import org.apache.http.NameValuePair;
        import org.apache.http.message.BasicNameValuePair;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;


        public class Sign_in extends FragmentActivity implements View.OnClickListener{

    Button btSignUp,btLogin;
    EditText etUser,etPass;

    // Progress Dialog
    private ProgressDialog nDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();



    //testing from a real server:

    private static final String LOGIN_URL = "http://www.eshteghel.comlu.com/dbservice/login.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);
        btSignUp = (Button) findViewById(R.id.btSignUp);
        btLogin = (Button) findViewById(R.id.btLogin);

        btSignUp.setOnClickListener(this);
        btLogin.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialog(){

        ClientDialog cd = new ClientDialog();
        cd.show(getSupportFragmentManager(), "DialogFragment");
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.btLogin:
                new AttemptLogin().execute();
                break;

            case R.id.btSignUp:
                showDialog();
                break;
            default:
                break;
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(Sign_in.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String username = etUser.getText().toString();
            String password = etPass.getText().toString();
            try {

                /*try {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    md5.update(password.getBytes(),0,password.length());
                    password = new BigInteger(1,md5.digest()).toString(16);
                    //System.out.println("Signature: "+signature);

                } catch (final NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }*/

                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                //Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // check your log for json response
                //Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    //Log.d("Login Successful!", json.toString());
                    Intent i = new Intent(Sign_in.this, Company.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                }else{
                    //Log.d("Login Failure!", json.getString(TAG_MESSAGE));
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
            nDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Sign_in.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }


}
