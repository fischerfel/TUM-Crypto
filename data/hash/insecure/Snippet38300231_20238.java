import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Nadeem Iqbal on 6/20/2016.
 */
public abstract class MyAbstractFacebookActivity extends MyAbstractActivity {

    private static String[] FB_BASIC_PERMISSIONS = new String[]{"public_profile", "email"};

    private CallbackManager callbackManager;
    private FacebookCallback<Sharer.Result> fbCallback;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private ProfileTracker profileTracker;
    private Profile profile;
    private String fbId = "";

    byte[] data;

    private boolean LOG_FB_HASH = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.e("FB HASH", getFBHashKey());
        fbInit();
    }


    public String getFBHashKey() {
        String key = "";
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                if (LOG_FB_HASH) {
                    copyToClipBoard(key);
                }
                log("KeyHash FB:", key);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
        }
        return key;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void copyToClipBoard(String textToCopy) {
        int sdk_Version = Build.VERSION.SDK_INT;
        if (sdk_Version < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(textToCopy);
            Toast.makeText(getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Text Label", textToCopy);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
        }
    }

    ///////// FB Work Start //////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ///// FB Work Start //////
        callbackManager.onActivityResult(requestCode, resultCode, data);
        ///// FB Work End //////

    }


    void fbInit() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        fbCallback = new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                log("Post Shared on FB");

                onPostSharedSuccessfully(result);
            }

            @Override
            public void onCancel() {
                toast("Cancelled");

                onPostSharedCancelled();
            }

            @Override
            public void onError(FacebookException e) {
                toast("Error:" + e.getMessage());
                e.printStackTrace();

                onPostSharedError(e);
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Profile.setCurrentProfile(currentProfile);
                profile = currentProfile;
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // On AccessToken changes fetch the new profile which fires the event on
                // the ProfileTracker if the profile is different
                Profile.fetchProfileForCurrentAccessToken();
            }
        };

        // Ensure that our profile is up to date
        Profile.fetchProfileForCurrentAccessToken();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        accessToken = AccessToken.getCurrentAccessToken();

                        onFBLoginSuccessfully(loginResult, accessToken);
                        getNewFBId();
                    }

                    @Override
                    public void onCancel() {
                        warning("Cancel", "User cancelled the process");

                        onFBLoginCancelled();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        warning("Error", "" + exception.getMessage());
                        onFBLoginError(exception);
                    }
                });
    }

    protected void getNewFBId() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            fbId = object.getString("id");
                            onFbId(fbId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public abstract void onFbId(String fbId);

    public void doLogin() {
        doLogin(FB_BASIC_PERMISSIONS);
    }

    public void doLogin(String[] permissions) {
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList(permissions));
    }

    ///////// FB Work End //////////

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public ProfileTracker getProfileTracker() {
        return profileTracker;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getFBId() {
        return fbId;
    }


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private static String getPrivacy(String privacy) {
        String str;
        if (privacy.equalsIgnoreCase("Everyone"))
            str = "EVERYONE";
        if (privacy.equalsIgnoreCase("Friends and Networks"))
            str = "NETWORKS_FRIENDS";
        else if (privacy.equalsIgnoreCase("Friends of Friends"))
            str = "FRIENDS_OF_FRIENDS";
        else if (privacy.equalsIgnoreCase("Friends Only"))
            str = "ALL_FRIENDS";
        else if (privacy.equalsIgnoreCase("Custom"))
            str = "CUSTOM";
        else if (privacy.equalsIgnoreCase("Specific People..."))
            str = "SOME_FRIENDS";
        else
            str = "SELF";

        return str;
    }

    public void shareVideoFB(String videoPath, final ProgressDialog pd) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        Uri fileUri = Uri.parse(videoPath);
        String fileName = videoPath.substring(videoPath.lastIndexOf('/') + 1, videoPath.length());
        String mimeType = getMimeType(videoPath);

        try {
            data = readBytes(videoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int videoSize = data.length;

        String privacy = getPrivacy("Friends Only");

        GraphRequest request = GraphRequest.newPostRequest(accessToken, "/me/videos", null, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                try {
                    if (pd.isShowing())
                        pd.dismiss();
                } catch (Exception e) {
                }

                onFBShareVideoCompleted(response);
            }
        });

        Bundle params = request.getParameters();

        params = request.getParameters();
        params.putByteArray(fileName, data);

        params.putString("title", fileName);
        params.putString("description", "Some Description...");

//        params.putString("upload_phase", "start");
        params.putInt("file_size", data.length);

        request.setParameters(params);
        request.executeAsync();

    }


    public byte[] readBytes(String dataPath) throws IOException {
        InputStream inputStream = new FileInputStream(dataPath);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }

    protected abstract void onFBShareVideoCompleted(GraphResponse response);    

    protected abstract void onFBLoginError(FacebookException exception);

    protected abstract void onFBLoginCancelled();

    protected abstract void onFBLoginSuccessfully(LoginResult loginResult, AccessToken accessToken);

    protected abstract void onPostSharedError(FacebookException e);

    protected abstract void onPostSharedCancelled();

    protected abstract void onPostSharedSuccessfully(Sharer.Result result);

}
