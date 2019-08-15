import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String PACKAGE = "com.android.app";
                                          // your package name

    Button login_linkedin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateHashkey();

        login_linkedin_btn = (Button) findViewById(R.id.login_button);
        login_linkedin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_linkedin();
            }
        });
    }

    // Authenticate with linkedin and intialize Session.

    public void login_linkedin(){
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {

                // Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAuthError(LIAuthError error) {

                Toast.makeText(getApplicationContext(), "failed " + error.toString(),
                                 Toast.LENGTH_LONG).show();
            }
        }, true);
    }

    // After complete authentication start new HomePage Activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this,
                                      requestCode, resultCode, data);
        Intent intent = new Intent(MainActivity.this,UserProfile.class);
        startActivity(intent);
    }

     // generate the hash key 
     public void generateHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    PACKAGE,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.d("Hask key",Base64.encodeToString(md.digest(), Base64.NO_WRAP));

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    // This method is used to make permissions to retrieve data from linkedin

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }
}
