public class ConnexionActivity extends AppCompatActivity {

    Button b_bb, b_back, ee, b_inscrp_formulaire, fb;
    EditText tb_pseudo, tb_mdp;
    int id;
    String name, gender, email, birthday;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    Profile profile;
    //new try:

    private AccessTokenTracker accessTokenTracker;
    private Button customFacebookLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        /*List<String> permissionNeeds = Arrays.asList("user_photos", "email",
                "user_birthday", "public_profile", "AccessToken");*/

        setContentView(R.layout.activity_connexion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        b_inscrp_formulaire = (Button) findViewById(R.id.b_inscrip_formulaire);


        fb = (Button) findViewById(R.id.fb);


        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setText("");

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends", "user_birthday"));
        //"user_photos", "email","user_birthday", "public_profile"
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                graphRequest(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }


        });


        //getting the hash key
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "your.package",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        //2nd way to generate hash key (or get it)
        try {
            PackageInfo info = getPackageManager().getPackageInfo("your.package.name", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        b_bb = (Button) findViewById(R.id.b_bb);
        b_back = (Button) findViewById(R.id.b_back);
        tb_pseudo = (EditText) findViewById(R.id.textbox_pseudo);
        tb_mdp = (EditText) findViewById(R.id.textbox_mdp);


        /////////////
        ee = (Button) findViewById(R.id.button);
        ////////////

        b_bb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        b_bb.setBackgroundResource(R.drawable.button_bb_selec);
                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        b_bb.setBackgroundResource(R.drawable.button_bb);

                        break;
                    }

                }
                return false;
            }
        });
        b_bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isAuthorized()) {

                    Intent menu_p = new Intent(ConnexionActivity.this, MenuPActivity.class);
                    menu_p.putExtra("pseudo", tb_pseudo.getText().toString());
                    startActivity(menu_p);
                } else {
                    openDialog();
                }

            }
        });


        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnexionActivity.this.finish();
            }
        });


        ee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu_p = new Intent(ConnexionActivity.this, MenuPActivity.class);
                menu_p.putExtra("pseudo", "Easter");
                startActivity(menu_p);

            }
        });

        b_inscrp_formulaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu_p = new Intent(ConnexionActivity.this, InscriptionActivity.class);
                startActivity(menu_p);

            }
        });


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                graphRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LoginManager.getInstance().logInWithReadPermissions(ConnexionActivity.this, Arrays.asList("user_photos", "email",
                        "user_birthday", "public_profile"));
                profile = Profile.getCurrentProfile().getCurrentProfile();
                if (profile != null) {
                    // user has logged in
                    Intent menu_p = new Intent(ConnexionActivity.this, MenuPActivity.class);
                    menu_p.putExtra("pseudo", profile.getFirstName());
                    startActivity(menu_p);

                    Toast.makeText(getApplicationContext(), "Connecté : " + profile.getFirstName() + " " + profile.getLastName(), Toast.LENGTH_SHORT).show();
                } else {
                    // user has not logged in
                    Toast.makeText(getApplicationContext(), "Non-Connecté : Erreur de connexion!!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        //To log out of FB:
        /*LoginManager.getInstance().logOut();*/

        //TODO: check if the user is logged in or not
       /* profile = Profile.getCurrentProfile().getCurrentProfile();
        if (profile != null) {
            // user has logged in
        } else {
            // user has not logged in
        }*/

    }

    private Boolean isAuthorized() {

        if (tb_pseudo.getText().toString().equals("Admin")
                && tb_mdp.getText().toString().equals("0000")
                ) {
            return true;
        } else
            return false;
    }

    public void openDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Mauvaise combinaison \"Pseudo\", \"Mot de passe\"");

        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void graphRequest(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {


                Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG).show();

            }
        });

        Bundle b = new Bundle();
        b.putString("fields", "id,email,first_name,last_name,picture.type(large)");
        request.setParameters(b);
        request.executeAsync();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

}
