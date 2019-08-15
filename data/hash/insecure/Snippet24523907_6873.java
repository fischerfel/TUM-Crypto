package com.rstm.doctorrx;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.doctorrx.integration.FacebookService;
import com.doctorrx.integration.FacebookService.FacebookFriendListRequester;
import com.doctorrx.integration.UserContext;
import com.doctorrx.integration.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class LoginActivity  extends Activity implements OnClickListener , FacebookFriendListRequester,
ConnectionCallbacks, OnConnectionFailedListener{

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    public static int socialLogin;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    ImageButton cross_btn;
    ProgressDialog dialog;
    Button login_btn, signup_btn,google_btn,facbook_login_btn;
    private boolean mSignInClicked;
    private static final int RC_SIGN_IN = 0;
    private SharedPreferences mPrefs;
    // Logcat tag
        private static final String TAG = "MainActivity";

        // Google client to interact with Google API
        private static  GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        cross_btn=(ImageButton)findViewById(R.id.cross_btn);
        login_btn=(Button)findViewById(R.id.login_btn);
        signup_btn=(Button)findViewById(R.id.register_btn);
        google_btn=(Button)findViewById(R.id.google_login_btn);
        facbook_login_btn=(Button)findViewById(R.id.facbook_login_btn);
        initialiseGoogleclient();
        facbook_login_btn.setOnClickListener(this);
        google_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        signup_btn.setOnClickListener(this);
        cross_btn.setOnClickListener(this);
//      try {
//          PackageInfo info = getPackageManager().getPackageInfo(
//                  "com.rstm.doctorrx", PackageManager.GET_SIGNATURES);
//          for (Signature signature : info.signatures) {
//              MessageDigest md = MessageDigest.getInstance("SHA");
//              md.update(signature.toByteArray());
//              Log.e("MY KEY HASH:",
//                      Base64.encodeToString(md.digest(), Base64.DEFAULT));
//          }
//      } catch (NameNotFoundException e) {
//
//      } catch (NoSuchAlgorithmException e) {}

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.facbook_login_btn:
            socialLogin=404;
            dialog = new ProgressDialog(this);
            loadMyFriendList();
            break;
        case R.id.cross_btn:
            startActivity(new Intent(v.getContext(),MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            break;
        case R.id.login_btn:
            startActivity(new Intent(v.getContext(),LoginDetailActivity.class));
            overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            break;
        case R.id.register_btn:
            startActivity(new Intent(v.getContext(),SignupActivity.class));
            overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            break;
        case R.id.google_login_btn:
            socialLogin=401;
            initialiseGoogleclient();
            signInWithGplus();
            break;

        }
    }

    private void initialiseGoogleclient() {
        // TODO Auto-generated method stub
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this).addApi(Plus.API)
        .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }
    private void loadMyFriendList() {
        // TODO Auto-generated method stub
        if (Utils.haveNetworkConnection(this)) {
            dialog.setMessage("Verifying credentials...");
            dialog.show();
            FacebookService.instance().setContext(getApplicationContext());
            if (!Utils.isAuthenticated()) {

                FacebookService.instance().fetchFacebookProfile(this);
            } else {
                UserContext.authorized = true;
                startActivity(new Intent(this,MyNewMainActivity.class));
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left);
                dialog.dismiss();
                finish();

            }
        }
    }
    @Override
    public void onListFetched(ArrayList<JSONObject> onLinefriends,
            ArrayList<JSONObject> idleLinefriends,
            ArrayList<JSONObject> offLinefriends) {
        // TODO Auto-generated method stub
        //dialog.dismiss();

    }
    @Override
    public void onFbError() {
        // TODO Auto-generated method stub
        dialog.dismiss();
    }
    public void onFacebookProfileRetreived(boolean isSuccess) {
        // override this method
        if (isSuccess) {

            startActivity(new Intent(this,MyNewMainActivity.class));
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left);
            dialog.dismiss();
            finish();
        } else {
            Toast.makeText(this, "Some Error occurred!", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(socialLogin==404){
        if (!UserContext.authorized) {
            FacebookService.instance().authorizeCallback(requestCode,
                    resultCode, data);
            UserContext.authorized = true;
            //dialog.dismiss();

        }
        }
        if(socialLogin==401)
        {
                if (requestCode == RC_SIGN_IN) 
                {
                    if (resultCode != RESULT_OK) {
                        mSignInClicked = false;
                    }

                    mIntentInProgress = false;

                    if (!mGoogleApiClient.isConnecting()) {
                        mGoogleApiClient.connect();
                    }
                }
        }

    }
//  public void onStart() {
//      super.onStart();
//      if (!Utils.haveNetworkConnection(this)) {
//          showNoConnectionDialog();
//      }
//  }

    private void showNoConnectionDialog() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("Error in Network Connection!").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.setTitle("Error!");
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();
    }
@Override
public void onConnectionFailed(ConnectionResult result) {
    // TODO Auto-generated method stub
    if (!result.hasResolution()) {
        GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                0).show();
        return;
    }

    if (!mIntentInProgress) {
        // Store the ConnectionResult for later usage
        mConnectionResult = result;
        if (mSignInClicked) {
            // The user has already clicked 'sign-in' so we attempt to
            // resolve all
            // errors until the user is signed in, or they cancel.
            resolveSignInError();
        }
    }
}


@Override
public void onConnected(Bundle paramBundle) {
    // TODO Auto-generated method stub
    mSignInClicked = false;

    // Get user's information
    getProfileInformation();
    if(socialLogin==401){
    startActivity(new Intent(this,MyNewMainActivity.class));
    overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left);
    finish();}
}
@Override
public void onConnectionSuspended(int paramInt) {
    // TODO Auto-generated method stub
    mGoogleApiClient.connect();
}

protected void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
}

protected void onStop() {
    super.onStop();
    if (mGoogleApiClient.isConnected()) {
        mGoogleApiClient.disconnect();
    }
}

/**
 * Method to resolve any signin errors
 * */
private void resolveSignInError() {
    if (mConnectionResult.hasResolution()) {
        try {
            mIntentInProgress = true;
            mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
        } catch (SendIntentException e) {
            mIntentInProgress = false;
            mGoogleApiClient.connect();
        }
    }
}

/**
 * Updating the UI, showing/hiding buttons and profile layout
 * */



/**
 * Fetching user's information name, email, profile pic
 * */
private void getProfileInformation() {

    try {
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi
                    .getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            String personPhotoUrl = currentPerson.getImage().getUrl();
            String personGooglePlusProfile = currentPerson.getUrl();
            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

            Log.e(TAG, "Name: " + personName + ", plusProfile: "
                    + personGooglePlusProfile + ", email: " + email
                    + ", Image: " + personPhotoUrl);


            // by default the profile url gives 50x50 px image only
            // we can replace the value with whatever dimension we want by
            // replacing sz=X
//          personPhotoUrl = personPhotoUrl.substring(0,
//                  personPhotoUrl.length() - 2);
            if(mPrefs == null){
                mPrefs = this.getSharedPreferences("MyGamePreferences", android.content.Context.MODE_PRIVATE);
            }
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putInt("login",401);
            editor.putString("Guser_name", personName);
            editor.putString("Guserpic_url", personPhotoUrl);
            editor.commit();
            UserContext.MyPicUrl=personPhotoUrl;
            UserContext.MyDisplayName=personName;
        } else {
            Toast.makeText(getApplicationContext(),
                    "Person information is null", Toast.LENGTH_LONG).show();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

/**
 * Sign-in into google
 * */
private void signInWithGplus() {
    if (!mGoogleApiClient.isConnecting()) {
        mSignInClicked = true;
        resolveSignInError();
    }
}

/**
 * Sign-out from google
 * */
public static void signOutFromGplus() {
    if (mGoogleApiClient.isConnected()) {
        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        mGoogleApiClient.disconnect();
        //mGoogleApiClient.connect();
    }
}
/**
 * Revoking access from google
 * */
public   void revokeGplusAccess() {
    if (mGoogleApiClient.isConnected()) {
        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status arg0) {
                        Log.e(TAG, "User access revoked!");
                        mGoogleApiClient.connect();
                    }

                });
    }
}



}
