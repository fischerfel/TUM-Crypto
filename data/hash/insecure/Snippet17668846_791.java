public class ResgistrationApp extends Activity {

    EditText username,password,name,nickname,email,phone;

    Button Registertauky,fb_connect;

    com.facebook.Session fbSession;
    ImageView user_image;
    private UiLifecycleHelper uihelper;

    private Session.StatusCallback sessioncallback=new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state, Exception exception) {
            // TODO Auto-generated method stub
            Log.e("Sessionstate", ""+state);
            if(session.getState()==SessionState.OPENED)
            {
                makeuserdetailsrequest(session);
            }

        }
    };

    private Request.GraphUserCallback graphcallback=new Request.GraphUserCallback() {

        @Override
        public void onCompleted(GraphUser user, Response response) {
            // TODO Auto-generated method stub
            Log.e("FB RES", ""+response);
            Log.e("UID", ""+user.getId());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) 

    {
        super.onCreate(savedInstanceState);
        //Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        setContentView(R.layout.activity_resgistration_app);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.primus.taukyresigstration", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        uihelper=new UiLifecycleHelper(this, sessioncallback);
        uihelper.onCreate(savedInstanceState);

        username=(EditText) findViewById(R.id.register_usernameone);

        password=(EditText) findViewById(R.id.register_password);

        name=(EditText) findViewById(R.id.register_username);

        nickname=(EditText) findViewById(R.id.register_nickname);

        email=(EditText) findViewById(R.id.register_email);

        phone=(EditText) findViewById(R.id.register_phone);

        Registertauky=(Button) findViewById(R.id.Registertauky);

        user_image=(ImageView) findViewById(R.id.register_user_picture);

        fb_connect=(Button) findViewById(R.id.facbook_getinfo);

        fbSession=new Session(this);
        fbSession.addCallback(sessioncallback);


        //This going to register the tauky server

        Registertauky.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });


        //This button click going to connect to the facebook
        fb_connect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub      
            if(fbSession.isOpened())
            {
                fbSession=Session.getActiveSession();
                //do the fetching of user details..
            }
            else
            {
                fbSession=Session.openActiveSession(ResgistrationApp.this, true, sessioncallback);
            }   
            }
        });
    }

    //--------------------------------------------------------------------------------------------------------------//



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        uihelper.onActivityResult(requestCode, resultCode, data);
        //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }



    class LoginretrieveTask extends AsyncTask<Void, Void, Void>
    {
        Session session;

        public LoginretrieveTask(Session fbsession) {
            // TODO Auto-generated constructor stub
            session=fbsession;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            return null;
        }

    }

    protected void makeuserdetailsrequest(Session session) {
        // TODO Auto-generated method stub

        Request user_request=Request.newMeRequest(session, graphcallback);
        user_request.executeAsync();
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        uihelper.onResume();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        uihelper.onPause();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        uihelper.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
        uihelper.onSaveInstanceState(savedInstanceState);
    }
}
