package com.example.prashant.nuhani_go;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.Signature;

import com.facebook.FacebookSdk;


public class learningActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        setContentView(R.layout.activity_learning);
        getUserInfo();
    }

    public void getUserInfo() {

        try{
        PackageInfo info= getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        for(Signature signature:info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("Key Hash:- ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }


        }
        catch (PackageManager.NameNotFoundException e) {}
        catch (NoSuchAlgorithmException e){}



    }


}