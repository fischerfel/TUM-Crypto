package com.devleb.encdecapp;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity implements OnClickListener {
    // views for the layout
    Spinner spin;
    EditText edit_txt_pass;
    static EditText edit_txt_enc_string;
    EditText edit_txt_raw;
    static EditText edit_txt_dec_string;
    Button btn_encrypt, btn_decrypt, btn_clear;

    private static SecretKey SKey;
    static String cyphertext = "";
    static String STReditTxtPass;
    String strPaddingencryption;
    static int iterations = 1000;
    private static final String[] items = { "Padding Key derivation",
            "SHA1PRNG key derivation", "PBKDF2 key derivation",
            "PKCS#12 key derivation" };

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String[] Passwords = { "password", "cryptography",
            "cipher", "algorithm", "qwerty" };

    // mesage that will be binded with the key to generate the cypher text
    private static String PlainText = "this is the text that will be encrypted";

    // the list that will be used for the OnItemSelection method
    private static final int PADDING_ENC_IDX = 0;
    private static final int SHA1PRNG_ENC_IDX = 1;
    private static final int PBKDF2_ENC_IDX = 2;
    private static final int PKCS12_ENC_IDX = 3;

    byte[] salt = { (byte) 0x11, (byte) 0x9B, (byte) 0xC6, (byte) 0xFE,
            (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x77 };;

    static byte[] ivBytes = { 0, 0, 0, 0, 0, 0, 0, 0 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SKey = kg.generateKey();

        // creation of the spinner with setting Array adapter and
        // DropDownresourse
        spin = (Spinner) findViewById(R.id.spiner);
        spin.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        aa.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spin.setAdapter(aa);
        // end of the spinner code

        edit_txt_pass = (EditText) findViewById(R.id.editTxtPass);
        edit_txt_enc_string = (EditText) findViewById(R.id.editTxtEncString);
        edit_txt_raw = (EditText) findViewById(R.id.editTxtRawKey);
        edit_txt_dec_string = (EditText) findViewById(R.id.editTxtDecString);

        btn_encrypt = (Button) findViewById(R.id.btnEncrypt);
        btn_encrypt.setOnClickListener(this);

        btn_decrypt = (Button) findViewById(R.id.btnDecrypt);
        btn_decrypt.setOnClickListener(this);

        btn_clear = (Button) findViewById(R.id.btnClear);
        btn_clear.setOnClickListener(this);

        // / for registering the editText to the Context Menu
        registerForContextMenu(edit_txt_pass);

    }

    // for the ciphering of the plainText using the base 64
    public static String toBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static byte[] fromBase64(byte[] bytes) {
        // return Base64.encodeToString(bytes, Base64.NO_WRAP);
        return Base64.decode(bytes, Base64.DEFAULT);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub

        int groupId = 0;
        menu.add(groupId, 1, 1, "password");
        menu.add(groupId, 2, 2, "cryptography");
        menu.add(groupId, 3, 3, "cipher");
        menu.add(groupId, 4, 4, "algorithm");
        menu.add(groupId, 5, 5, "qwerty");

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        return getText(item);

        // return super.onContextItemSelected(item);
    }

    private boolean getText(MenuItem item) {
        // TODO Auto-generated method stub

        int menuItemId = item.getItemId();

        if (menuItemId == 1) {
            edit_txt_pass.setText("password");
        }
        if (menuItemId == 2) {
            edit_txt_pass.setText("cryptography");
        }
        if (menuItemId == 3) {
            edit_txt_pass.setText("cipher");
        }
        if (menuItemId == 4) {
            edit_txt_pass.setText("algorithm");
        }
        if (menuItemId == 5) {
            edit_txt_pass.setText("qwerty");
        }
        STReditTxtPass = edit_txt_pass.getText().toString();

        Log.w("the String of the Password text", STReditTxtPass);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if (v == btn_encrypt) {

            encryptPadding(PlainText, salt);
        } else if (v == btn_clear) {
            edit_txt_enc_string.setText("");
        } else if (v == btn_decrypt) {
            decryptPadding(cyphertext, salt);
        }
    }

    public static String encryptPadding(String plaintext, byte[] salt) {
        try {

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, SKey);

            byte[] cipherText = cipher.doFinal(PlainText.getBytes("UTF-8"));

            cyphertext = String.format("%s%s%s", toBase64(salt), "]",
                    toBase64(cipherText));
            edit_txt_enc_string.setText(cyphertext);
            return cyphertext;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptPadding(String ctext, byte[] salt) {
        try {

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, SKey, ivSpec);

            byte[] plaintxt = cipher.doFinal(cyphertext.getBytes("UTF-8"));

            PlainText = String.format("%s%s%s", fromBase64(salt), "]",
                    fromBase64(plaintxt));
            edit_txt_dec_string.setText(PlainText);
            return PlainText;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
