public class SplashActivity extends Activity {

    private Intent intent;
    private Handler mHandler;
    private Runnable mNextRunnable;
    private final long SPLASH_TIME = 3000;
    private VideoView videoHolder;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ImageView iv;
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash);

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "info.nexrave.nexrave",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (Exception e) {
//
//        }

        Log.d(TAG, "Video about to be called");
        //Video settings
        videoHolder = (VideoView) findViewById(R.id.videoView);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.video_footage);
        videoHolder.setVideoURI(video);
        videoHolder.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoHolder.start();

        //Facebook settings
        iv = (ImageView) findViewById(R.id.iv_splash_logo);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile", "email", "user_friends");
        Log.d(TAG, "UpdateWithToken Called: registering callback");
        loginButton.registerCallback(callbackManager, mCallBack);
//        AppEventsLogger.activateApp(this);
        Log.d(TAG, "UpdateWithToken About To Be Called");
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
//                updateWithToken(newAccessToken);
//            }
//        };
        updateWithToken(AccessToken.getCurrentAccessToken());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void makeVisible() {
        iv.setVisibility(View.VISIBLE);
        iv.bringToFront();
        loginButton.setVisibility(View.VISIBLE);
        loginButton.bringToFront();
    }

    private void updateWithToken(AccessToken currentAccessToken) {
        Log.d(TAG, "UpdateWithToken Called");
        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Log.d(TAG, "UpdateWithToken Called: User logged in");
                    Intent i = new Intent(SplashActivity.this, FeedActivity.class);
                    startActivity(i);
                }
            }, SPLASH_TIME);

        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Log.d(TAG, "UpdateWithToken Called: Not logged in");
                    makeVisible();
                    Log.d(TAG, "UpdateWithToken Called: finished");
                }
            }, SPLASH_TIME);
        }
    }

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // App code
            Log.d(TAG, "UpdateWithToken Called: Done");
            //Placeholder: Need to check firebase to see if already registered
            intent = new Intent(SplashActivity.this, EnterPhoneNumber.class);
            startActivity(intent);
        }

        @Override
        public void onCancel() {
            // App code
            Log.d(TAG, "UpdateWithToken Called: cancel");
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
            Log.d(TAG, "UpdateWithToken Called: error");
        }
    };
}
