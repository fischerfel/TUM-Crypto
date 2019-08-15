import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
               PackageInfo info = getPackageManager().getPackageInfo("com.key", PackageManager.GET_SIGNATURES);
               for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());

                      TextView tvmyName = (TextView)findViewById(R.id.KeyText);
                      tvmyName.setText(Base64.encodeBytes(md.digest()));


               }
            } catch (NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }

    }
}
