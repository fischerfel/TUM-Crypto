public class SpleshScreen extends Activity {


@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_splesh_screen);

    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "mypackage", 
                PackageManager.GET_SIGNATURES);
        for (android.content.pm.Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.wtf("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
    } catch (NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }

    Thread background = new Thread() {

        public void run() {

            try {
                // Thread will sleep for 2 seconds
                sleep(2 * 1000);


                //
                // Log.e("asdasdasdasd",my_json );

                // After 2 seconds redirect to another intent
                Intent in = new Intent(getApplicationContext(),
                        MainmoviesActivity.class);
                startActivity(in);

                // Remove activity
                finish();

            } catch (Exception e) {

            }
        }
    };

    // start thread
    background.start();
}
