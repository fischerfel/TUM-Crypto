import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import santana.gujarat.chatmeet.R;

/**
 * Created by root on 14/05/15.
 */
public class LoginActivity extends AppCompatActivity implements GraphRequest.GraphJSONObjectCallback {


    private static final List<String> permissionNeeds= Arrays.asList("user_photos", "friends_photos", "email", "user_birthday", "user_friends");

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private GraphRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_activity);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.facebook.samples.loginhowto",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(permissionNeeds);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                loginResult.getAccessToken();

                request = GraphRequest.newMeRequest(loginResult.getAccessToken(), LoginActivity.this);
                request.executeAsync();

                Log.d("FB Granted", loginResult.getRecentlyGrantedPermissions().toString());
                Log.d("FB Denied", loginResult.getRecentlyDeniedPermissions().toString());
                AccessToken.getCurrentAccessToken();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                Log.d("Error Login",e.getMessage());

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }


    @Override
    public void onCompleted(JSONObject user, GraphResponse response) {
        Log.d("resultFB", user.toString());
        try {
            Log.d("resultFB",user.getString("username"));
        } catch (JSONException e) {

        }

    }

}
