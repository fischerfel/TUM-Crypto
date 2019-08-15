public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkAppSignature(this) == true){
            Toast.makeText(getApplicationContext(), "invalid", Toast.LENGTH_LONG).show();
        }

        else{

            Toast.makeText(getApplicationContext(), "valid", Toast.LENGTH_LONG).show(); }

    }


    public static final String SIGNATUREE = "aqKTfwHKzuY52bukXMaRKgXPQyA=";

    public static boolean checkAppSignature(Context context) {

      try {

        PackageInfo packageInfo = context.getPackageManager()

            .getPackageInfo(context.getPackageName(),

                PackageManager.GET_SIGNATURES);

        for (Signature signature : packageInfo.signatures) {

          byte[] signatureBytes = signature.toByteArray();

          MessageDigest md = MessageDigest.getInstance("SHA");

          md.update(signature.toByteArray());

          final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);

          //compare signatures

          if (SIGNATUREE.equals(currentSignature)){

              return false;
          }

      }

        } catch (Exception e) {

      }

      return true;
    }

}
