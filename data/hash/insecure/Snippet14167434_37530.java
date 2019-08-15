package com.mattle.timetable;

import static com.mattle.timetable.MainActivity.PREFS_NAME;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gcm.GCMRegistrar;
import com.markupartist.android.widget.ActionBar;

public class LoginView extends Activity {

private JSONParser jsonParser;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login_view);

    ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
    // You can also assign the title programmatically by passing a
    // CharSequence or resource id.
    actionBar.setTitle("Jouw Lesrooster");
}

@Override
public void onBackPressed() {

    Intent startMain = new Intent(Intent.ACTION_MAIN);
    startMain.addCategory(Intent.CATEGORY_HOME);
    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(startMain);

}

 private String getStringResourceByName(String aString)
    {
      String packageName = "com.mattle.timetable";
      int resId = getResources().getIdentifier(aString, "string", packageName);
      return getString(resId);
    }

private boolean haveNetworkConnection() {
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;

    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
    for (NetworkInfo ni : netInfo) {
        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
            if (ni.isConnected())
                haveConnectedWifi = true;
        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
            if (ni.isConnected())
                haveConnectedMobile = true;
    }
    return haveConnectedWifi || haveConnectedMobile;
}

public String sha1(String s) {
    MessageDigest digest = null;
            try {
                    digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
    digest.reset();
    byte[] data = digest.digest(s.getBytes());
    return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
}

public void doRegister(View view) {

    Intent gotoregister = new Intent(this, Register.class);
    startActivity(gotoregister);

}

public void doLogin(View view) throws Exception {

    BufferedReader in = null;

    try{

        if(haveNetworkConnection()) {

            EditText getusername = (EditText) findViewById(R.id.username);
            String username = getusername.getText().toString();

            EditText getpassword = (EditText) findViewById(R.id.password);
            String password = sha1(getpassword.getText().toString() + "SOMESALTHERENVM");

        HttpClient client = new DefaultHttpClient();
        URI website = new URI("SOMEURL");
        HttpPost request = new HttpPost();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("login_username", username));
        nameValuePairs.add(new BasicNameValuePair("login_password", password));

        jsonParser = new JSONParser();
        JSONObject json = jsonParser.getJSONFromUrl("http://jouwlesrooster.nl/api/doLogin/", nameValuePairs);

        try {

            if (json.getString("success") != null) {
                String res = json.getString("success");
                if(Integer.parseInt(res) == 1){
            //Log.d("MYTAG", "Print this in logcat...");
            //Log.d("MYTAG", username + " ddd " + password);

            getSharedPreferences("myLoginshit",MODE_PRIVATE)
            .edit()
            .putString("Username", username)
            .putString("Password", password)
            .commit();

            GCMRegistrar.checkDevice(this);
            GCMRegistrar.checkManifest(this);
            String regId = GCMRegistrar.getRegistrationId(this);
            if (regId.equals("")) {
              GCMRegistrar.register(this, "***");
              Log.i("test", "ddd");
             // Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            } else {
            //  Toast.makeText(getApplicationContext(), "Already registered", Toast.LENGTH_LONG).show();
             // Log.v(TAG, "Already registered");
            }

            Intent gotomenu = new Intent(this, Menu.class);
            startActivity(gotomenu);

            }
            }

        } finally {

        }

        } else {


        }

    }finally{
        if (in != null) {
            try{
                in.close();

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
}
