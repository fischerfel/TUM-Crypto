// you should either define client id and secret as constants or in string resources
private final String clientId = "xxxxxxxxxxxxxxxxx";
private final String responseType = "code";

/**
 * same as in manifest in intent filter
 */
private final String redirectUri = "http://www.example.com/gizmos";

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "xxxxxxxxxxxxxxx",  // replace with your unique package name
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }

    Button loginButton = (Button) findViewById(R.id.loginbutton);
    loginButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(ServiceGenerator.API_BASE_URL + "/dialog/oauth" +
                            "?client_id=" + clientId +
                            "&redirect_uri=" + redirectUri +
                            "&response_type=" + responseType));
            startActivity(intent);
        }
    });
}

@Override
protected void onResume() {
    super.onResume();

    // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
    Uri uri = getIntent().getData();
    if (uri != null && uri.toString().startsWith(redirectUri)) {
        // use the parameter your API exposes for the code (mostly it's "code")
        String code = uri.getQueryParameter("code");
        if (code != null) {
            Log.i("code", code);
            // get access token
            // we'll do that in a minute
        } else if (uri.getQueryParameter("error") != null) {
            // show an error message here
        }
    }
}
