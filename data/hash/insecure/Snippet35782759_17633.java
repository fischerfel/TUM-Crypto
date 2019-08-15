public class Facebook extends FragmentActivity {

private BlankFragment mainFragment;
private LoginButton loginButton;
private CallbackManager callbackManager;


public String name;
public String id;
public String imageUrl;



@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_facebook);



    FacebookSdk.sdkInitialize(getApplicationContext());
    callbackManager = CallbackManager.Factory.create();


    loginButton = (LoginButton) findViewById(R.id.login_button);

    printHashkey();

    if (savedInstanceState == null) {
        // Add the fragment on initial activity setup
        mainFragment = new BlankFragment();
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, mainFragment).commit();
    } else {
        // Or set the fragment from restored state info
        mainFragment = (BlankFragment) getSupportFragmentManager()
                .findFragmentById(android.R.id.content);
    }



}


public void printHashkey(){
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "name",
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




@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
}



public void goToAttract(View v)
{
    Profile profile = Profile.getCurrentProfile();
    getProfile(profile);

    Intent intent = new Intent(this, MainActivity.class);
    Bundle b = new Bundle();
    b.putString("Nome", name);
    b.putString("ID", id);
    intent.putExtras(b);

    setResult(1, intent);
    finish();
}



public void getProfile(Profile profile){
    if(profile != null){
        id = profile.getId().toString();
        name = profile.getName().toString();

    }
}


}
