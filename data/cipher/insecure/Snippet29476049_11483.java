package com.androidedsoft.aesencryptor;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import android.util.Base64;

import org.apache.commons.codec.Decoder;
import org.apache.commons.codec.Encoder;

public class MainActivity extends ActionBarActivity {
    public static SecretKey secretKey;
    static Cipher cipher;

Button encryptbutton;
String plainText;

public static void main(String[] args) throws Exception {

    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128); //key is 128 bit
    SecretKey secretKey = keyGenerator.generateKey();
    cipher = Cipher.getInstance("AES"); //sets as AES encryption type
}

public void btnClick() {
    encryptbutton = (Button) findViewById(R.id.encryptbutton);
    encryptbutton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
