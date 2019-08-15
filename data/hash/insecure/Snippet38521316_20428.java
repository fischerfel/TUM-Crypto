public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       
        facebookOncreateCalling();

       
        ImageView facebookLogin = (ImageView) findViewById(R.id.facebook_login_imageView2);
        facebookLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

              


               try {
                   LoginManager.getInstance().logOut();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email" /*, "user_mobile_phone", "email", "user_birthday"*/));
            }
        });

    }

   
   
    public void hideSoftKeyboard(EditText inputtext) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputtext.getWindowToken(), 0);
    }

    
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;

    private void facebookOncreateCalling() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Facebook KeyHash:", "KeyHash:-> " + Base64.encodeToString(md.digest(), Base64.DEFAULT));

               
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        callGraphApi(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Log.e("====Login Activity===","Cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("====Login Activity===","Error"+exception);
                    }
                });

        
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
            }
        };
        accessToken = AccessToken.getCurrentAccessToken();
    }

   
    private void callGraphApi(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
      
                            Log.e("LoginActivity", "GraphRequest:" + object);

                           

                            String fbUserId = object.optString("id");
                            String firstNameString = object.optString("first_name");
                            String lastNameString = object.optString("last_name");
                            String email = object.optString("email");

                            String name = object.optString("name");

                            String url = AppDataUtill.APP_BASE_URL + "facebookSignup";
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("fname",firstNameString);
                            map.put("lname",lastNameString);
                            map.put("email",email);
                            map.put("fb_user_id", fbUserId);

                            callFbSignupApi(url, map, 1, fbUserId);
                           


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,link,birthday,gender,email");
        request.setParameters(parameters);
        request.executeAsync();
    }


   


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


        
    }

   
    private void validation(){

        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
        alert.setMessage("Enter Your Email"); 

       
        final EditText input = new EditText(getApplicationContext());
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                
                String srt = input.getEditableText().toString();
                if (srt.matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter Your Email", Toast.LENGTH_LONG).show();
                    return;
                }

            } 
        }); 
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                
                dialog.cancel();
            }
        }); 
        AlertDialog alertDialog = alert.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();



    }



}