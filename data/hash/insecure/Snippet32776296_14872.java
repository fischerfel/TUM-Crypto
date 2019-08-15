          public class Login extends ActionBarActivity {
// flag for Internet connection status
Boolean isInternetPresent = false;

// Connection detector class
ConnectionDetector cd;
ImageView imgview;
EditText uname;
EditText pass;
Button create,login;
TextView trouble;
public static final String MyPREFERENCES = "MyPrefs";
SharedPreferences sharedpreferences;
private static final String TAG = "myAppSurun";

//Drawer
//private ListView mDrawerList;
//private DrawerLayout mDrawerLayout;
//private ActionBarDrawerToggle mDrawerToggle;
private String mActivityTitle;

//End Drawer



//Asynchronous task variable
// Progress Dialog
private ProgressDialog pDialog;

// Creating JSON Parser object
JSONParser jParser = new JSONParser();

ArrayList<HashMap<String, String>> productsList;

    String userid,otpkey,vrfy;
// url to get all products list
private static String url_all_login = "http://xxx/xxx/xxx/xxx";


//Globalstring
String username =null;
String password = null;

//Global Variable for login  state checking
public static boolean loginflag = false;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    cd = new ConnectionDetector(getApplicationContext());
    imgview=(ImageView)findViewById(R.id.imageView2);
    uname=(EditText)findViewById(R.id.edituser);
    pass=(EditText)findViewById(R.id.editpassword);
    create=(Button)findViewById(R.id.create);
    login=(Button)findViewById(R.id.Login);
    trouble=(TextView)findViewById(R.id.trouble_login);
    getSupportActionBar().setTitle("Surun Support");
    //getSupportActionBar().setDisplayShowHomeEnabled(true);
    //getSupportActionBar().setLogo(R.drawable.dotlogo1);
    //getSupportActionBar().setDisplayUseLogoEnabled(true);
    //ActionBar bar = getSupportActionBar();
    //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F58634")));

    // Start Drawer Settings
    //mDrawerList = (ListView) findViewById(R.id.navListlog);
    //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_login);
    //mActivityTitle = getTitle().toString();
    //addDrawerItems();
    //setupDrawer();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    ActionBar bar = getSupportActionBar();
    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F58634")));
    // End of Drawer Settings

    //Click Text Animation
    final Animation myanim, imganim;
    myanim = AnimationUtils.loadAnimation(this, R.anim.link_text_anim);
    imganim = AnimationUtils.loadAnimation(this, R.anim.rotate);
    //End of animation

    //Initialize the component

    uname.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {


            validation.isValid(uname, "^[_a-z]+(\\.[_a-z0-9-]+)*@[a-z]+(\\.[a-z]+)*(\\.[a-z]{2,})$", "Invalid UserName", true);


        }
    });

    pass.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validation.isValid(pass, "[0-9]{10}", "Invalid Mobile No", true);
        }
    });

    //end of initializing component

   // Creating underlined text
   // String udata = "Create Account";
    //SpannableString content = new SpannableString(udata);
    //content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
   //create.setText(content);



    //end of creating underlined text
    //Toast.makeText(getApplicationContext(), "Create", Toast.LENGTH_LONG).show();
    //Log.v("TEST", "TEST");
    create.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Create Click", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), Registration_user.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);


        }
    });//End of On click for button

    //starting font settings this has prone to error try catch is mandatory while setting font(Overriding native font interface).
   try {
        Typeface myTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/robotoregular.ttf");
        create.setTypeface(myTypeface);
    } catch (Exception e) {
        Log.v(TAG, "Exception " + e);
    }
    //End of font settings

    //Initializing shared preferences
    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    //load preferences if exist
    Log.v(TAG,""+sharedpreferences);
    Log.v(TAG,"Preference is present loading it");
    String shareduser = sharedpreferences.getString("User", "");
    String sharedpass = sharedpreferences.getString("Password", "");
    String shareduid = sharedpreferences.getString("userid", "");
    String sharedotp = sharedpreferences.getString("otp", "");
    String sharedvrfy = sharedpreferences.getString("verified", "");
    Log.v(TAG,"uid"+shareduser);
    Log.v(TAG,"otp"+sharedpass);


    if ((shareduser.length() > 0) && (sharedpass.length() > 0)) {
        //Navigating to main page
        Log.v(TAG,"navigate to main");
        Intent i = new Intent(getApplicationContext(), UserLogedIn.class);
        i.putExtra("user", shareduser);
        i.putExtra("pass", sharedpass);
        i.putExtra("userid", shareduid);
        i.putExtra("otpkey",sharedotp);
        i.putExtra("vrfy", sharedvrfy);
        //Starting An Activity
        startActivity(i);
        finish();
    } else {
    login.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
           // get Internet status
             isInternetPresent = cd.isConnectingToInternet();

             // check for Internet status
             if (isInternetPresent) {
                 if (uname.getText().length() <= 0 || pass.getText().length() <= 0) {
                     AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();

                     // Setting Dialog Title
                     alertDialog.setTitle("Alert Dialog");

                     // Setting Dialog Message
                     alertDialog.setMessage("All Fields Are Mandatory");

                     // Setting Icon to Dialog
                     // alertDialog.setIcon(R.drawable.tick);

                     // Setting OK Button
                     alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             // Write your code here to execute after dialog closed
                             Toast.makeText(getApplicationContext(), "Please Enter Correct User Name And Password", Toast.LENGTH_SHORT).show();
                         }
                     });

                     // Showing Alert Message
                     alertDialog.show();

                     if (pass.getText().length() > 10) {
                         Toast toast1 = Toast.makeText(getApplicationContext(), "Four Characters Only...", Toast.LENGTH_SHORT);
                         toast1.show();
                         trouble.setVisibility(View.VISIBLE);
                         //Log.v(TAG,"Not Valid");

                     }
                 } else if (pass.getText().length() > 10) {
                     AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();

                     // Setting Dialog Title
                     alertDialog.setTitle("Alert Dialog");

                     // Setting Dialog Message
                     alertDialog.setMessage("Mobile no Must be 10 digit only ");

                     // Setting Icon to Dialog
                     // alertDialog.setIcon(R.drawable.tick);

                     // Setting OK Button
                     alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             // Write your code here to execute after dialog closed
                             Toast.makeText(getApplicationContext(), "Please Enter Correct User Name And Password", Toast.LENGTH_SHORT).show();
                         }
                     });

                     // Showing Alert Message
                     alertDialog.show();

                 } else if (!(validation.isValid(uname, "^[_a-z]+(\\.[_a-z0-9-]+)*@[a-z]+(\\.[a-z]+)*(\\.[a-z]{2,})$", "Invalid UserName", true))) {
                     AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();

                     // Setting Dialog Title
                     alertDialog.setTitle("Alert Dialog");

                     // Setting Dialog Message
                     alertDialog.setMessage("Email Is Incorrect");

                     // Setting Icon to Dialog
                     // alertDialog.setIcon(R.drawable.tick);

                     // Setting OK Button
                     alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             // Write your code here to execute after dialog closed
                             Toast.makeText(getApplicationContext(), "Email Id Is InCorrect", Toast.LENGTH_SHORT).show();
                             uname.requestFocus();
                         }
                     });

                     // Showing Alert Message
                     alertDialog.show();
                 } else {
                     //Animate Button load animation from anim/rotate.xml
                     imgview.startAnimation(imganim);
                     username = uname.getText().toString();
                     password = pass.getText().toString();

                     //Sending Login Request To Server for validation Using Asynchronus Tasks where username and password as a parameter to method
                     Log.v(TAG, "Excuting check detail");
                     new CheckDetail().execute();

                     //Creating Shared Preferences


                 }//end of else_if fields are valid


             }
             else
             {
                 AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();

                 // Setting Dialog Title
                 alertDialog.setTitle("Alert Dialog");

                 // Setting Dialog Message
                 alertDialog.setMessage("Internet is not active.Please Check Your NEtwork Setting");

                 // Setting Icon to Dialog
                 // alertDialog.setIcon(R.drawable.tick);

                 // Setting OK Button
                 alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {
                         // Write your code here to execute after dialog closed
                         Toast.makeText(getApplicationContext(), "Internet Is Inactive", Toast.LENGTH_SHORT).show();

                     }
                 });

                 // Showing Alert Message
                 alertDialog.show();
             }
         }
     });//End of On click for button
    }//If no shared preferences found

}//End of onCreate function

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_admin_home, menu);
    return true;
}//End of onCreateOptionMenu

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

    //Remove Following Comment To Enable Drawer Toggling On Login Page

    /*// Activate the navigation drawer toggle
    if (mDrawerToggle.onOptionsItemSelected(item)) {
        return true;
    }*/

    return super.onOptionsItemSelected(item);
}//End of onOptionItemSelected

//Alert Dialog when User Click Back Button
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    //Handle the back button
    if(keyCode == KeyEvent.KEYCODE_BACK) {
        //Ask the user if they want to quit
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.quit)
                .setMessage(R.string.really_quit)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Stop the activity
                        Login.this.finish();
                    }

                })
                .setNegativeButton("no", null)
                .show();

        return true;
    }
    else {
        return super.onKeyDown(keyCode, event);
    }

}//End of Alert Dialog Box



/**
 * Background Async Task to Login by making HTTP Request
 */

class CheckDetail extends AsyncTask<String,String, String> {

    /**
     * Before starting background thread Show Progress Dialog
     */

    JSONArray datail=null;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Logging in. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    /**
     * getting All products from url
     */
    protected String doInBackground(String... args) {
        try {
            Log.v(TAG, "In Do in Background");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", username));
            params.add(new BasicNameValuePair("pwd", password));
            // getting JSON string from URL
            JSONObject json =jParser.makeHttpRequest(url_all_login, "POST", params);
            //Object Parsing Failed here hence by using trail guide using JSONParser.alternateJSONArray to parse user data.
            //Hence not using json instance of object using a BACKUP static variable of Parser class for proccessing.
            //To use this backup utility theme the process should be standard and return unique or two out put only or ether way use three logical step
            if(json != null) {
                // As if login fails it returns object handling fail logic here.
                //We can make it general by sending array from server side so we can only use alternateJSONArray variable

            }
             if(JSONParser.alternateJSONArray != null)
            {
                Log.v(TAG, "USING BACKUP ARRAY");
                //Check your log cat for JSON futher details
                for (int jsonArrayElementIndex=0; jsonArrayElementIndex < JSONParser.alternateJSONArray.length(); jsonArrayElementIndex++) {
                    JSONObject jsonObjectAtJsonArrayElementIndex = JSONParser.alternateJSONArray.getJSONObject(jsonArrayElementIndex);
                     userid=jsonObjectAtJsonArrayElementIndex.getString("u_id");
                    otpkey=jsonObjectAtJsonArrayElementIndex.getString("OTP");
                    vrfy=jsonObjectAtJsonArrayElementIndex.getString("is_verified");
                    Log.v(TAG,"" +userid);
                    Log.v(TAG,"" +otpkey);
                    Log.v(TAG,"" +vrfy);
                    if(jsonObjectAtJsonArrayElementIndex.getString("email").equals(username) && jsonObjectAtJsonArrayElementIndex.getString("mobile").equals(password))
                    {
                        Log.v(TAG,"Login Successful Now setting loginflag true");
                        loginflag = true;
                    }
                }


            }
            else
            {
                //JSON is null ether no data or 204 returned by server
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, "Exception at end :" + e.toString());
            //Log.e("TAG", "Error......!RecoverIt");
        }

        return null ;
    }


    protected void onPostExecute(String result)
    {
        // dismiss the dialog after getting all products
        //super.onPostExecute();
        // pDialog.dismiss();

        if(loginflag==true) {
                Log.v(TAG, "Executing Shared Preferences...");
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("User", uname.getText().toString());
                editor.putString("Password", pass.getText().toString());
                Log.v(TAG, "" + userid);
                Log.v(TAG, "" + otpkey);
                Log.v(TAG, "" + vrfy);

                editor.commit();
                Toast.makeText(getApplicationContext(), "Login Succeed", Toast.LENGTH_SHORT).show();
                android.util.Log.v(TAG, "Login Succeed");
                Intent i = new Intent(getApplicationContext(), UserLogedIn.class);
                startActivity(i);
                finish();
            }

        else
        {
            Toast.makeText(getApplicationContext(),"Login failed,Invalid Details...!",Toast.LENGTH_LONG).show();
            trouble.setVisibility(View.VISIBLE);
        }
        pDialog.dismiss();
    }

}



String getMD5(String pass) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(pass.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        String hashtext = number.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}
   /* private int returnParsedJsonObject(String result) {

        JSONObject resultObject = null;

        int returnedResult = 0;

        try {

            resultObject = new JSONObject(result);

            returnedResult = resultObject.getInt("success");

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return returnedResult;

    }*/
    }
