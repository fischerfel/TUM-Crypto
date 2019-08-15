package facebooklocation.facebooklocation;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CallbackManager callbackManager;
    ImageView iv_image, iv_facebook;
    TextView tv_name, tv_email, tv_dob, tv_location, tv_facebook;
    LinearLayout ll_facebook;
    String str_facebookname, str_facebookemail, str_facebookid, str_birthday, str_location;
    boolean boolean_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        getKeyHash();
        listener();
    }


    private void init() {
        iv_image = (ImageView) findViewById(R.id.iv_image);
        iv_facebook = (ImageView) findViewById(R.id.iv_facebook);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_dob = (TextView) findViewById(R.id.tv_dob);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_facebook = (TextView) findViewById(R.id.tv_facebook);
        ll_facebook = (LinearLayout) findViewById(R.id.ll_facebook);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
    }

    private void listener() {
        tv_facebook.setOnClickListener(this);
        ll_facebook.setOnClickListener(this);
        iv_facebook.setOnClickListener(this);

    }

    private void facebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("ONSUCCESS", "User ID: " + loginResult.getAccessToken().getUserId()
                        + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken()
                );
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    boolean_login = true;
                                    tv_facebook.setText("Logout from Facebook");

                                    Log.e("object", object.toString());
                                    str_facebookname = object.getString("name");

                                    try {
                                        str_facebookemail = object.getString("email");
                                    } catch (Exception e) {
                                        str_facebookemail = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        str_facebookid = object.getString("id");
                                    } catch (Exception e) {
                                        str_facebookid = "";
                                        e.printStackTrace();

                                    }


                                    try {
                                        str_birthday = object.getString("birthday");
                                    } catch (Exception e) {
                                        str_birthday = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        JSONObject jsonobject_location = object.getJSONObject("location");
                                        str_location = jsonobject_location.getString("name");

                                    } catch (Exception e) {
                                        str_location = "";
                                        e.printStackTrace();
                                    }

                                    fn_profilepic();

                                } catch (Exception e) {

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email,gender,birthday,location");

                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                if (AccessToken.getCurrentAccessToken() == null) {
                    return; // already logged out
                }
                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile,email"));
                        facebookLogin();

                    }
                }).executeAsync();


            }

            @Override
            public void onError(FacebookException e) {
                Log.e("ON ERROR", "Login attempt failed.");


                AccessToken.setCurrentAccessToken(null);
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile,email,user_birthday"));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {

        }

    }

    private void getKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("facebooklocation.facebooklocation", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void fn_profilepic() {

        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        params.putString("type", "large");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/picture",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        Log.e("Response 2", response + "");

                        try {
                            String str_facebookimage = (String) response.getJSONObject().getJSONObject("data").get("url");
                            Log.e("Picture", str_facebookimage);

                            Glide.with(MainActivity.this).load(str_facebookimage).skipMemoryCache(true).into(iv_image);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        tv_name.setText(str_facebookname);
                        tv_email.setText(str_facebookemail);
                        tv_dob.setText(str_birthday);
                        tv_location.setText(str_location);

                    }
                }
        ).executeAsync();
    }


    @Override
    public void onClick(View view) {

        if (boolean_login) {
            boolean_login = false;
            LoginManager.getInstance().logOut();
            tv_location.setText("");
            tv_dob.setText("");
            tv_email.setText("");
            tv_name.setText("");
            Glide.with(MainActivity.this).load(R.drawable.profile).into(iv_image);
            tv_facebook.setText("Login with Facebook");
        } else {
            LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile,email,user_birthday,user_location"));
            facebookLogin();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }
}
