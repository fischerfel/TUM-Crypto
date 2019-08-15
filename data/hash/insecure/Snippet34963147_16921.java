public class Login extends AppCompatActivity {
TextView tv_sign_up;
private EditText inputName, inputEmail, input_contact_no, input_address, input_city;
private TextInputLayout inputLayoutName, inputLayoutEmail, input_layout_city, input_layout_contact_no, input_layout_address;
private Button btnSignUp;
private CallbackManager callbackManager;
public static String FEmail;
private AccessTokenTracker accessTokenTracker;
public static ProfileTracker profileTracker;
public static String Name, Email, ImgURL, FbID;

static SharedPreferences settings;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    settings = getSharedPreferences(Splash.PREFS_NAME, 0);
    boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);


    if (hasLoggedIn) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    FacebookCall();

    Loaduielements();

    LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
    loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
    loginButton.registerCallback(callbackManager, callback);

    btnSignUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            submitForm();
        }
    });
}

private void FacebookCall() {

    FacebookSdk.sdkInitialize(getApplicationContext().getApplicationContext());

    showHashKey(getApplicationContext());
    callbackManager = CallbackManager.Factory.create();

    accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

        }
    };

    profileTracker = new ProfileTracker() {
        @Override
        protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
            displayMessage(newProfile);
        }
    };

    accessTokenTracker.startTracking();
    profileTracker.startTracking();
    setContentView(R.layout.login);

}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);

}

@Override
public void onStop() {
    super.onStop();
    accessTokenTracker.stopTracking();
    profileTracker.stopTracking();
}

@Override
public void onResume() {
    super.onResume();
    Profile profile = Profile.getCurrentProfile();
    displayMessage(profile);
}

public static void showHashKey(Context context) {
    try {
        PackageInfo info = context.getPackageManager().getPackageInfo(
                "com.example.tryitonjewelry", PackageManager.GET_SIGNATURES); //Your            package name here
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {
    } catch (NoSuchAlgorithmException e) {
    }
}

private void displayMessage(Profile profile) {

    if (profile != null) {

        FbID = profile.getId();

        try {

            Name=profile.getName();
            FbID = profile.getId();
            ImgURL = "https://graph.facebook.com/" + FbID + "/picture?type=large";
            SharedClass.setDefaults("ImgURL", "" + ImgURL, this);

        } catch (NetworkOnMainThreadException e) {

            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}

private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
    @Override
    public void onSuccess(LoginResult loginResult) {
        AccessToken accessToken = loginResult.getAccessToken();
        Profile profile = Profile.getCurrentProfile();

        // Facebook Email address
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        Log.v("LoginActivity Response ", response.toString());

                        try {
                            Name = object.getString("name");
                            SharedClass.setDefaults("Name", "" + Name, getApplicationContext());
                            FEmail = object.getString("email");
                            Log.v("Email = ", " " + FEmail);
                            Toast.makeText(getApplicationContext(), "Name " + Name, Toast.LENGTH_LONG).show();
                            SharedClass.setDefaults("Email", "" + FEmail, getApplicationContext());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();

        displayMessage(profile);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

        SharedPreferences settings = getSharedPreferences(Splash.PREFS_NAME, 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean("hasLoggedIn", true);

        editor.commit();

    }

    @Override
    public void onCancel() {
        LoginManager.getInstance().logOut();

    }

    @Override
    public void onError(FacebookException e) {

    }
};

private void Loaduielements() {

    inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
    input_layout_contact_no = (TextInputLayout) findViewById(R.id.input_layout_contact_no);

    tv_sign_up = (TextView) findViewById(R.id.tv_sign_up);
    inputEmail = (EditText) findViewById(R.id.input_email);
    input_contact_no = (EditText) findViewById(R.id.input_contact_no);

    btnSignUp = (Button) findViewById(R.id.btn_signup);

    inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
    input_contact_no.addTextChangedListener(new MyTextWatcher(input_contact_no));

    settings = getSharedPreferences(Splash.PREFS_NAME, 0);
    boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);


    if (hasLoggedIn) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}

public void Sign_Up_Clicked(View view) {
    Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
    tv_sign_up.startAnimation(fadeout);
    tv_sign_up.setTextColor(Color.parseColor("#F7A305"));
    Intent r = new Intent(getApplicationContext(), Register.class);
    startActivity(r);

}

/**
 * Validating form
 */
private void submitForm() {
    if (!validateEmail()) {
        return;
    }
    /*if (!validateName()) {
        return;
    }*/

    if (!contactnumber()) {
        return;
    }
    /*if (!address()) {
        return;
    }
    if (!city()) {
        return;
    }*/

    Intent i = new Intent(getApplicationContext(), MainActivity.class);
    startActivity(i);
    finish();

    SharedPreferences settings = getSharedPreferences(Splash.PREFS_NAME, 0); // 0 - for private mode
    SharedPreferences.Editor editor = settings.edit();

    editor.putBoolean("hasLoggedIn", true);

    editor.commit();
}

private boolean validateEmail() {
    String email = inputEmail.getText().toString().trim();

    if (email.isEmpty() || !isValidEmail(email)) {
        inputLayoutEmail.setError(getString(R.string.err_msg_email));
        requestFocus(inputEmail);
        return false;
    } else {
        inputLayoutEmail.setErrorEnabled(false);
    }

    return true;
}


private boolean contactnumber() {
    if (input_contact_no.getText().toString().trim().isEmpty()) {
        input_layout_contact_no.setError(getString(R.string.err_msg_contact_no));
        requestFocus(input_contact_no);
        return false;
    } else {
        input_layout_contact_no.setErrorEnabled(false);
    }
    return true;
}


private static boolean isValidEmail(String email) {
    return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
}

private void requestFocus(View view) {
    if (view.requestFocus()) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}

private class MyTextWatcher implements TextWatcher {

    private View view;

    private MyTextWatcher(View view) {
        this.view = view;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {
        switch (view.getId()) {
        /*  case R.id.input_name:
                validateName();
                break;*/
            case R.id.input_email:
                validateEmail();
                break;
            case R.id.input_contact_no:
                contactnumber();
                break;
            /*case R.id.input_address:
                address();
                break;
            case R.id.input_city:
                city();
                break;*/

        }
    }
}
