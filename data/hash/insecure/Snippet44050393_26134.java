    package chat.demo.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;


public class FBLogin extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener {
    PlusClient  mPlusClient;
    private UiLifecycleHelper uiHelper;
    private static final String TAG = "MainFragment";
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    TextView txtskip;
    GoogleCloudMessaging gcm;
    String regid, name, username, location, lastname, logintype, profileurl,
            gender, city, country;
    String PROJECT_NUMBER = DataManager.PROJECT_NUMBER;
    private ProgressDialog progress;
    String msg = "";
    SharedPreferences prefs;
    SessionManager session;
    Button btnlogin, register;
    // google+ login
    private static final int RC_SIGN_IN = 0;
    GPSTracker gps;
    String strLatitude, strLongitude;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    private boolean mSignInClicked;
    private SignInButton btnSignIn;
    Geocoder geocoder;
    double longitude = 0, latitude = 0;
    private String deviceid = "";
    Button btnregister, btnemailsignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fblogin);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            callFacebookLogout(getApplicationContext());
            mGoogleApiClient.disconnect();
            signOutFromGplus();
        }
        deviceid = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

        geocoder = new Geocoder(this, Locale.getDefault());
        gps = new GPSTracker(this);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getRegId();

        getlocation();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "chat.demo.app", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

         btnregister = (Button) findViewById(R.id.btnregister);
        btnemailsignin = (Button) findViewById(R.id.btnemailsignin);
        LoginButton lgnbtn = (LoginButton) findViewById(R.id.loginButton);

        btnSignIn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                signInWithGplus();
            }
        });

        btnregister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i = new Intent(FBLogin.this, RegisterActivity.class);
                finish();
                startActivity(i);
                overridePendingTransition(0, 0);

            }
        });

        btnemailsignin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i = new Intent(FBLogin.this, LoginActivity.class);
                finish();
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });

    }

    public static void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                // clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
            // clear your preferences if saved

        }

    }

    private void signOutFromGplus() {

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(Status arg0) {
                            // TODO Auto-generated method stub
                            mGoogleApiClient.disconnect();
                        }

                    });
        }
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = false;
            return;

        }



    }

    private void signInWithGplus() {

        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();

        }
    }

    public void getlocation() {

        if (gps.canGetLocation()) {
            try {
                if (gps.canGetLocation()) {
                    Log.d("Your Location", "latitude:" + gps.getLatitude()
                            + ", longitude: " + gps.getLongitude());
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                }

                if (latitude == 0.0 && longitude == 0.0) {
                    city = "";
                    country = "";
                } else {
                    List<Address> addresses;
                    addresses = geocoder
                            .getFromLocation(latitude, longitude, 1);

                    System.out.println("address..." + addresses.toArray());

                    city = addresses.get(0).getAddressLine(2);
                    country = addresses.get(0).getCountryName();

                }

                System.out.println("city----" + city);
                System.out.println("country----" + country);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
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
                username = currentPerson.getId();
                name = currentPerson.getDisplayName();
                lastname = "";
                profileurl = currentPerson.getImage().getUrl();
                int sex = currentPerson.getGender();
//              city = currentPerson.getCurrentLocation();

                System.out.println("sex---" + sex);
                System.out.println("city---" + city);

                if(sex == 0)
                {
                    gender = "male";
                }else
                {
                    gender = "female";
                }

                logintype = "google";

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", username: " + username
                        + ", Image: " + personPhotoUrl);

                profileurl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2) + 150;

                new login().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                                startActivity(intent);
                            }
                        }).create().show();
    }

    private void onSessionStateChange(Session session, SessionState state,
            Exception exception) {
        if (state.isOpened()) {

            Request.newMeRequest(session, new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                    // TODO Auto-generated method stub

                    if (user != null) {

                        username = user.getId();
                        name = user.getFirstName();
                        lastname = user.getLastName();
                        profileurl = username;
                        gender = user.getProperty("gender").toString();
                        Log.i(TAG, "username..."+username);
                        logintype = "facebook";

                        new login().execute();
                    }
                }
            }).executeAsync();

        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        // mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public boolean isLoggedIn() {
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }

        else {
            uiHelper.onActivityResult(requestCode, responseCode, data);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {

        mGoogleApiClient.connect();

    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void resolveSignInError() {

        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(mConnectionResult.getResolution()
                        .getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
            } catch (SendIntentException e) {
                // The intent was canceled before it was sent. Return to the
                // default
                // state and attempt to connect to get an updated
                // ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }

    }

    public void getRegId() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging
                                .getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                    System.out.println("Error---" + ex.getMessage());
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                System.out.println("Registerid---" + regid);
            }
        }.execute(null, null, null);

    }

    public class login extends AsyncTask<String, Void, String> {
        boolean response = false;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(FBLogin.this, "Register...",
                    "Please wait....");

        }

        @Override
        protected String doInBackground(String... params) {

            registernotification();

            return "";

        }

        @Override
        protected void onPostExecute(String result) {

            progress.cancel();

            DataManager.username = username;
            scheduleAlarm();
            if (logintype.equals("facebook")) {
                if (isLoggedIn()) {
                    session.createLoginSession(username, name, lastname,
                            deviceid);
                    session.setlogintype(logintype);
                    session.setProfilepic(profileurl);
                    Intent i = new Intent(FBLogin.this, MainActivity.class);
                    i.setAction("splash");
                    finish();
                    startActivity(i);
                }
            } else if (logintype.equals("google")) {
                session.setlogintype(logintype);
                session.createLoginSession(username, name, lastname, deviceid);
                session.setProfilepic(profileurl);
                Intent i = new Intent(FBLogin.this, MainActivity.class);
                i.setAction("splash");
                finish();
                startActivity(i);
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    public void registernotification() {

        String url = DataManager.url + "register.php";
        HttpClient client = new DefaultHttpClient();
        HttpPost get = new HttpPost(url);

        List<NameValuePair> paramas = new ArrayList<NameValuePair>();
        paramas.add(new BasicNameValuePair("gcmid", regid));
        paramas.add(new BasicNameValuePair("userid", username));
        paramas.add(new BasicNameValuePair("firstname", name));
        paramas.add(new BasicNameValuePair("lastname", lastname));
        paramas.add(new BasicNameValuePair("profilepic", profileurl));
        paramas.add(new BasicNameValuePair("logintype", logintype));
        paramas.add(new BasicNameValuePair("gender", gender));
        paramas.add(new BasicNameValuePair("city", city));
        paramas.add(new BasicNameValuePair("country", country));
        paramas.add(new BasicNameValuePair("deviceid", deviceid));
        UrlEncodedFormEntity ent;
        try {
            ent = new UrlEncodedFormEntity(paramas, HTTP.UTF_8);
            get.setEntity(ent);
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String returnString = null;
        HttpResponse response = null;
        try {
            response = client.execute(get);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                returnString = EntityUtils.toString(resEntity);
                System.out.println("returnString---" + returnString.toString());
                try {

                    JSONObject obj = new JSONObject(returnString);

                    String status = obj.getString("status");

                    SharedPreferences.Editor se = prefs.edit();
                    se.putString("status", status);
                    se.commit();

                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void connectionerror() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FBLogin.this);

        alertDialog.setTitle("Error!");

        alertDialog.setMessage("Connection Lost ! Try Again");

        alertDialog.setPositiveButton("Retry",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new login().execute();

                    }
                });

        alertDialog.show();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        getProfileInformation();

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress) {
            // Store the ConnectionResult so that we can use it later when the
            // user clicks
            // 'sign-in'.
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    public void scheduleAlarm() {

        Long time = new GregorianCalendar().getTimeInMillis() + 1 * 60 * 1000;

        Intent intentAlarm = new Intent(this, AlarmReciever.class);

        // create the object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intentAlarm, 0);

        try {
            alarmManager.cancel(pi);
        } catch (Exception e) {
            Log.e("Cancel",
                    "AlarmManager update was not canceled. " + e.toString());
        }
        // set the alarm for particular time
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent
                .getBroadcast(this, 1, intentAlarm,
                        PendingIntent.FLAG_UPDATE_CURRENT));

    }
}
