public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout activity_main;
    LinearLayout loginScreenLinearLayout;
    EditText placeholder_username;
    EditText placeholder_password;
    Button login;
    LinearLayout linearLayoutFB;
    LinearLayout linearLayoutGoogle;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Log.d("KeyHash",printKeyHash(MainActivity.this));
    }

   public void initView()
   {
       activity_main=(RelativeLayout) findViewById(R.id.activity_main);
       loginScreenLinearLayout=(LinearLayout) findViewById(R.id.loginScreenLinearLayout);
       int paddingTop=calculatePaddingHeight();
       loginScreenLinearLayout.setPadding(0,paddingTop,0,0);
       placeholder_username=(EditText) findViewById(R.id.userName);
       placeholder_password=(EditText) findViewById(R.id.password);
       login=(Button)findViewById(R.id.login);
       linearLayoutFB=(LinearLayout) findViewById(R.id.linearLayoutFB);
       linearLayoutGoogle=(LinearLayout) findViewById(R.id.linearLayoutGoogle);
       FacebookSdk.sdkInitialize(getApplicationContext());
       callbackManager=CallbackManager.Factory.create();
       login.setOnClickListener(this);
       linearLayoutFB.setOnClickListener(this);
       linearLayoutGoogle.setOnClickListener(this);

   }

   public int calculatePaddingHeight()
   {
       int height = this.getResources().getDisplayMetrics().heightPixels;
       int sixtyPercentHeight=height*60/100;
       return height-sixtyPercentHeight;
   }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }
    public void loginThroughFB()
    {
        LoginManager.getInstance().registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
               Log.d("Success","Logged in successful");
            }

            @Override
            public void onCancel() {
                Log.d("Cancel","Logged in cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Error","Logged in error");
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.linearLayoutFB:
                if (ApplicationUtility.checkConnection(MainActivity.this)) {
                loginThroughFB();
                } else{
                    showLoginSnackBar(MainActivity.this, "No Internet Connection", activity_main);
                }
                break;
           case R.id.linearLayoutGoogle:
                break;
        }
    }

    public static void showLoginSnackBar(final Activity context, String message, View view) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.DKGRAY);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
