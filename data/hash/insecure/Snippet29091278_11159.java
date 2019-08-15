public class MyApplication extends Application implements
ConnectionCallbacks, OnConnectionFailedListener,
ResultCallback<People.LoadPeopleResult>{

    public static Typeface app_medium;
    public static Typeface app_regular;
    public static Typeface app_bold;

    public static final String TAG = MyApplication.class.getSimpleName();

    private static SharedPreferences Pref;

    private static MyApplication mInstance;

    private static final int RC_SIGN_IN = 0;

    // Google client to communicate with Google
    public static GoogleApiClient mGoogleApiClient;
    public boolean mIntentInProgress;
    public static boolean signedInUser;
    public static ConnectionResult mConnectionResult;

    @SuppressWarnings("unused")
    public void onCreate() {

        super.onCreate();

        mInstance = this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API, Plus.PlusOptions.builder().build())
        .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        Pref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());


        checkFBKey();

        app_regular = Typeface.createFromAsset(getAssets(),
                "fonts/dax_regular.ttf");

        app_medium = Typeface.createFromAsset(getAssets(),
                "fonts/dax_medium.ttf");

        app_bold = Typeface.createFromAsset(getAssets(),
                "fonts/dax_bold.ttf");

    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    /**
     * set user login
     * */
//  public static void setUserFBLogin() {
//      // TODO Auto-generated method stub
//      Editor edit_login_detail = Pref.edit();
//      edit_login_detail.putBoolean(GeneralClass.temp_iUserFaceBookBLOGIN,
//              true);
//      edit_login_detail.commit();
//  }



    public void checkFBKey() {

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                // String something = new
                // String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

    }

    public static void googlePlusLogin() {
        if (!mGoogleApiClient.isConnecting()) {
            signedInUser = true;
        }
    }

    public static void googlePlusLogout() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
//          updateProfile(false);
        }
    }

    public static void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e("LOGIN", "User access revoked!");
                            mGoogleApiClient.connect();
                        }

                    });
        }
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

    @Override
    public void onResult(LoadPeopleResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
        if (!arg0.hasResolution()) {
            return;
        }

        if (!mIntentInProgress) {
            // store mConnectionResult
            MyApplication.mConnectionResult = arg0;

            if (signedInUser) {
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        Log.e("APPLICATION", "CONNECTED");
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        mGoogleApiClient.connect();
    }


}
