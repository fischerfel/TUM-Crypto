import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Set;

public class FbManager {

    private Context context;
    private static final String LOGTAG = "FbManager";
    private CallbackManager callbackManager;
    private int retryCount = 0;

    public FbManager(Context context, CallbackManager callbackManager){
        this.context = context;
        this.callbackManager = callbackManager;
    }

    public static void traceKeyHash(Activity activity){
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo("com.xxx.android", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i(LOGTAG, "Share - KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void share(final String msg) {

        retryCount = 0;

        if (isLoggedIn()) {
            post(msg);
        }
        else{
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(LOGTAG, "facebook login success");
                    post(msg);
                }

                @Override
                public void onCancel() {
                    Log.w(LOGTAG, "facebook login canceled");
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.e(LOGTAG, "facebook login error");
                    exception.printStackTrace();
                }
            });

            LoginManager.getInstance().logInWithPublishPermissions((Activity) context, Arrays.asList("publish_actions"));
        }
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void post(final String msg){
        Log.d(LOGTAG, "facebook posting new message");
        Set<String> permissions = AccessToken.getCurrentAccessToken().getPermissions();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        Bundle postParams = new Bundle();
        postParams.putString("message", msg);
        postParams.putString("picture", "http://lh5.ggpht.com/-YP1POFWKolqatDOsX5yKYwvR6k1kj5dL4yxeKkBiL8sp6JE3l8Ty9tJt-zqzI2jj1Uu=w300");
        postParams.putString("link", "http://goo.gl/FYssFq");

        GraphRequest.Callback callback = new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                if (graphResponse.getError()!=null){
                    Log.e(LOGTAG, "facebook posting new message error");
                    Log.e(LOGTAG, graphResponse.getError().getErrorMessage());
                    retryCount++;
                    if (retryCount<3){
                        Log.d(LOGTAG, "facebook posting new message retry number " + retryCount);
                        post(msg);
                    }
                }
                else{
                    Log.d(LOGTAG, "facebook posting new message success");
                }
            }
        };

        GraphRequest request = new GraphRequest(accessToken, "me/feed", postParams, HttpMethod.POST, callback);
        AsynTaskGraphRequest asynTaskGraphRequest = new AsynTaskGraphRequest(request);
        asynTaskGraphRequest.execute();
    }
}
