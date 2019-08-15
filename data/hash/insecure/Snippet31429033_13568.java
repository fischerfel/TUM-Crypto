package com.example.personalvison.socialaccount;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends ActionBarActivity implements       View.OnClickListener, GoogleApiClient.ConnectionCallbacks,  GoogleApiClient.OnConnectionFailedListener {


    //Google Plus Account INtegration
    private static final int RC_SIGN_IN = 0;
    private static final int PROFILE_PIC_SIZE = 400;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private SignInButton btnSignIn;
    private Button btnSignOut;
    private ImageView imgProfilePic;
    private TextView txtName, txtGender, txtEmialaddress;


    private UiLifecycleHelper uiHelper;
    private View otherView;
    private LoginButton facebooklogin;
    private static final String TAG = "MainActivity";
    private Facebook facebook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(MainActivity.this)
                  .addOnConnectionFailedListener(MainActivity.this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        txtName = (TextView) findViewById(R.id.name);
        txtGender = (TextView) findViewById(R.id.gender);
        txtEmialaddress = (TextView) findViewById(R.id.location);
        otherView = (View) findViewById(R.id.other_views);
        otherView.setVisibility(View.GONE);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        facebooklogin = (LoginButton) findViewById(R.id.authButton);
        facebooklogin.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    txtName.setText("Hello, " + user.getName());
                } else {
                    txtName.setText("You are not logged");
                }
            }
        });
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEy HASH :--", Base64.encodeToString(md.digest(), Base64.DEFAULT) + "PacakgeName:" + getPackageName());
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private Session.StatusCallback callback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);

        }
       };

    private void onSessionStateChange(final Session session, SessionState state,
                                      Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in..."+session.getAccessToken());

            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {

                    if (user != null) {
                        otherView.setVisibility(View.VISIBLE);
                        txtName.setText("Hello " + user.getName());
                        txtGender.setText("Your Gender: "
                                + user.getProperty("gender").toString());
                        txtEmialaddress.setText("Your Emial Address:" + user.getProperty("email"));
                        Log.i(TAG, "Logged in..." + session.getAccessToken());
                    }
                }
            }).executeAsync();
        } else if (state.equals(SessionState.CLOSED_LOGIN_FAILED)) {
            Log.e(TAG, "Closed Login Failed");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out..." + session.getAccessToken());
            otherView.setVisibility(View.GONE);
            session.closeAndClearTokenInformation();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Activity", "Start");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Activity", "Stop");
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Activity", "preexcute");
        uiHelper.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }
            mIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Activity", "Resume");
        uiHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Activity", "Pause");
        uiHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Activity", "Destroy");
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Activity", "SaveInstanceState");
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                signInWithGplus();
                break;
            case R.id.btn_sign_out:
                signOutFromGplus();
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        getProfileInformation();
        updateUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateUI(false);
        }
        otherView.setVisibility(View.GONE);
    }

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
                txtName.setText("NAME:" + personName);
                txtEmialaddress.setText("E-mail:" + email);
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                otherView.setVisibility(View.VISIBLE);
                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        }
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }
        if (!mIntentInProgress) {
            mConnectionResult = result;
            if (mSignInClicked) {
                resolveSignInError();
            }
        }
    }
}
