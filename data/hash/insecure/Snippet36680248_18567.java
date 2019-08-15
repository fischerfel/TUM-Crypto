package com.powerofpixels.magpie;


import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Tom Finet on 16/04/2016.
 */
public class Magpie extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        /* Initialize Firebase */
        Firebase.setAndroidContext(getApplicationContext());
        /* Enable disk persistence  */
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        // print out the key hash, to prevent hackers from accessing user information
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.powerofpixels.magpie",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
