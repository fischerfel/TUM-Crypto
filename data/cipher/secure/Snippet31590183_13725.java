package com.example.rsatest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStore.SecretKeyEntry;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.DropBoxManager.Entry;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
String keyStoreFile;
Key privateKey = null;
boolean isUnlocked = false;
KeyStore keyStore = null;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    keyStoreFile = this.getFilesDir() + "/bpstore.keystore";
    try {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            startActivity(new Intent("android.credentials.UNLOCK"));
            isUnlocked = true;
        } else {
            startActivity(new Intent("com.android.credentials.UNLOCK"));
            isUnlocked = true;
        }
    } catch (ActivityNotFoundException e) {
        Log.e("TAG", "No UNLOCK activity: " + e.getMessage(), e);
        isUnlocked = false;
    }

    if(isUnlocked){
        privateKey = GetPrivateKey();

        try{
            char[] pw =("123").toCharArray();
            keyStore = createKeyStore(this,keyStoreFile, pw);
            PasswordProtection keyPassword = new PasswordProtection("pw-secret".toCharArray());

            SecretKey sk = new SecretKey() {

                @Override
                public String getFormat() {
                    // TODO Auto-generated method stub
                    return privateKey.getFormat();
                }

                @Override
                public byte[] getEncoded() {
                    // TODO Auto-generated method stub
                    return privateKey.getEncoded();
                }

                @Override
                public String getAlgorithm() {
                    // TODO Auto-generated method stub
                    return privateKey.getAlgorithm();
                }
            };
            System.out.println(sk.getEncoded());
            System.out.println(privateKey.getEncoded());
            KeyStore.SecretKeyEntry ent = new SecretKeyEntry(sk);
            keyStore.setEntry("pk", ent, keyPassword);
            keyStore.store(new FileOutputStream(keyStoreFile), pw);

            KeyStore keyStore2;
            keyStore2 = KeyStore.getInstance("BKS");
            keyStore2.load(new FileInputStream(keyStoreFile), pw);
            KeyStore.Entry entry = keyStore2.getEntry("pk", keyPassword);
            KeyStore.SecretKeyEntry entOut = (KeyStore.SecretKeyEntry)entry;
        }catch(Exception ex){
            System.out.println("Error: " + ex.toString());
        }

    }

}


@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
        return true;
    }
    return super.onOptionsItemSelected(item);
}

private KeyStore createKeyStore(Context context, String fileName, char[] pw) throws Exception {
    System.out.println("[DIR]:" + fileName);
    File file = new File(fileName);

    keyStore = KeyStore.getInstance("BKS");

    if (file.exists()) 
    {
        keyStore.load(new FileInputStream(file), pw);
    } else 
    {
        keyStore.load(null, null);
        keyStore.store(new FileOutputStream(fileName), pw);
    }

    return keyStore;
}

private Key GetPrivateKey(){
    String theTestText = "This is just a simple test!";

    Key publicKey = null;

    Key privateKey = null;
    try {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
    } catch (Exception e) {
        Log.e("", "RSA key pair error");
    }

    // Encode the original data with RSA private key
    byte[] encodedBytes = null;
    try {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, privateKey);
        encodedBytes = c.doFinal(theTestText.getBytes());
    } catch (Exception e) {
        Log.e("", "RSA encryption error");
    }

    // Decode the encoded data with RSA public key
    byte[] decodedBytes = null;
    try {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.DECRYPT_MODE, publicKey);
        decodedBytes = c.doFinal(encodedBytes);
    } catch (Exception e) {
        Log.e("", "RSA decryption error");
    }
    return privateKey;
}
}
