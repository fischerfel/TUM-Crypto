private CallbackManager callbackManager;
private ShareDialog shareDialog;
private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
    @Override
    public void onCancel() {
        Log.d("HelloFacebook", "Canceled");
    }

    @Override
    public void onError(FacebookException error) {
        Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
    }

    @Override
    public void onSuccess(Sharer.Result result) {
        Log.d("HelloFacebook", "Success!");
    }
};

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    getHashKey();

    findViewById(R.id.btn_post).setOnClickListener(this);

    FacebookSdk.sdkInitialize(this.getApplicationContext());
    callbackManager = CallbackManager.Factory.create();

    shareDialog = new ShareDialog(this);
    shareDialog.registerCallback(callbackManager, shareCallback);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
}

@Override
public void onClick(View v) {
    postStatus();
}

private void postStatus() {
    ShareLinkContent linkContent = new ShareLinkContent.Builder()
            .setContentTitle("Hello UTU")
            .setContentDescription("The 'Hello Facebook' sample  showcases simple Facebook integration")
            .setContentUrl(Uri.parse("http://static.comicvine.com/uploads/original/11114/111141352/3551664-iron-man-6800-hd-wallpapers.jpg"))
            .build();

    if (ShareDialog.canShow(ShareLinkContent.class)) {
        shareDialog.show(linkContent);
    }
}

private void getHashKey() {
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "com.facebook.samples.hellofacebook",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
}
