package com.example.amdroid.route;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

private CallbackManager callbackManager;
private LoginButton fbLoginButton;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    FacebookSdk.sdkInitialize(getApplicationContext());
    callbackManager = CallbackManager.Factory.create();


    //You need this method to be used only once to configure
    //your key hash in your App Console at
    // developers.facebook.com/apps

    getFbKeyHash("com.example.amdroid.route");

    setContentView(R.layout.activity_main);
    fbLoginButton = (LoginButton)findViewById(R.id.fb_login_button);

    fbLoginButton.registerCallback(callbackManager, new         
    FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            System.out.println("Facebook Login Successful!");
            System.out.println("Logged in user Details : ");
            System.out.println("--------------------------");
            System.out.println("User ID  : " +   
            loginResult.getAccessToken().getUserId());
            System.out.println("Authentication Token : " +                  
            loginResult.getAccessToken().getToken());
            Toast.makeText(MainActivity.this, "Login Successful!",  
            Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "Login cancelled by user!", 
            Toast.LENGTH_LONG).show();
            System.out.println("Facebook Login failed!!");

        }

        @Override
        public void onError(FacebookException e) {
            Toast.makeText(MainActivity.this, "Login unsuccessful!", 
            Toast.LENGTH_LONG).show();
            System.out.println("Facebook Login failed!!");
        }
    });
}





public void getFbKeyHash(String packageName) {

    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("YourKeyHash :", Base64.encodeToString(md.digest(), 
            Base64.DEFAULT));
            System.out.println("YourKeyHash: "+  
            Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }

}

@Override
protected void onActivityResult(int reqCode, int resCode, Intent i) {
    callbackManager.onActivityResult(reqCode, resCode, i);
}
}
