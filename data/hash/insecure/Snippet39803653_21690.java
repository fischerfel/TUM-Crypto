import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    FacebookSdk.sdkInitialize(getApplicationContext());
    AppEventsLogger.activateApp(this);
   printKeyHash();
}


private void printKeyHash() {
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                getPackageName(), PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.i("KeyHash:",
                    Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {
        Log.e("jk", "Exception(NameNotFoundException) : " + e);
    } catch (NoSuchAlgorithmException e) {
        Log.e("mkm", "Exception(NoSuchAlgorithmException) : " + e);
    }
}
