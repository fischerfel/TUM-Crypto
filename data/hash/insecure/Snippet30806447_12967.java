Public class learningActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        getUserInfo();
    }

    public void getUserInfo() {

        try{
        PackageInfo info= getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        for(Signature signature:info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("Key Hash:- ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }


        }
        catch (PackageManager.NameNotFoundException e) {}
        catch (NoSuchAlgorithmException e){}



    }


}
