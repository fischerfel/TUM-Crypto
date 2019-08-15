package com.project.qrcode

import android.security.KeyStore;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ZXingLibConfig zxingLibConfig;
    private Handler handler = new Handler();
    private TextView txtScanResult;
    KeyStore ks = KeyStore.getInstance();
    SecretKeyStore secretKeyStore = new SecretKeyStore();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

    byte[] hashedBytes;
    String decoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
              if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                startActivity(new Intent("android.credentials.UNLOCK"));
              } else {
                startActivity(new Intent("com.android.credentials.UNLOCK"));
              }
            } catch (ActivityNotFoundException e) {
                Log.e(getPackageName(), "No UNLOCK activity: " + e.getMessage(), e);
        }

        zxingLibConfig = new ZXingLibConfig();
        zxingLibConfig.useFrontLight = true;
        txtScanResult = (TextView) findViewById(R.id.scan_result);

        Button scanButton = (Button) findViewById(R.id.scan_button);

        //Set a listener on the scan button
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfKeyStored()) {
                    Toast keyerror = Toast.makeText(getBaseContext(), "You need to complete setup first", Toast.LENGTH_SHORT);
                    keyerror.show();
                    return;
                }
                IntentIntegrator.initiateScan(MainActivity.this, zxingLibConfig);
            }
        });
        Log.v(getPackageName(), "Listener set on scan button");

        Button setupButton = (Button) findViewById(R.id.setup_button);

        // Set a listener on the setup button
        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfKeyStored()) {
                    Log.v(getPackageName(), "Key is already stored");
                    Toast keyerror = Toast.makeText(getBaseContext(), "You have already completed setup", Toast.LENGTH_SHORT);
                    keyerror.show();
                    return;
                }
                Log.v(getPackageName(), "Key not stored, proceeding with setup");
                IntentIntegrator.initiateScan(MainActivity.this, zxingLibConfig);
            }
        });
        Log.v(getPackageName(), "Listener set on setup button");
    }

    protected boolean checkIfKeyStored() {
        String[] keyNames = ks.saw("");

        if( keyNames.length == 0 ) {
            return false;
        }

        return true;
    }

    // IF setup is done i.e. key is stored send to server
    // Otherwise store on phone

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v(getPackageName(), "Scanned QRCode");

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (scanResult == null) {
                Log.v(getPackageName(), "Scanned nothing");
                return;
            }

            //Contents of the QRCode
            Log.v(getPackageName(), "Scan complete, getting result");
            final String result = scanResult.getContents();
            Log.v(getPackageName(), "Scanned the following code "+ result);

            //If there is already a secret key stored i.e. setup already done
            if (checkIfKeyStored()) {
                Log.v(getPackageName(), "Key already stored, encrypting");
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA1PRNG");
                    Log.v(getPackageName(), "Got SHA1PRNG instance");

                    byte[] keyBytes = ks.get("twofactorkey"); 
                    byte[] resultBytes = result.getBytes("UTF-8");

                    Log.v(getPackageName(), "Got Bytes");

                    outputStream.write( resultBytes );
                    outputStream.write( keyBytes );
                    Log.v(getPackageName(), "Wrote Bytes to output stream");

                    byte[] bytesToEncrypt = outputStream.toByteArray( );
                    Log.v(getPackageName(), "Wrote to Byte array");

                    hashedBytes = digest.digest(bytesToEncrypt);
                    decoded = new String(hashedBytes, "UTF-8");
                    Log.v(getPackageName(), "Coverted bytes to String");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        txtScanResult.setText(decoded);
                        Log.v(getPackageName(), "Set TextView");
                    }
                });
            }
            else //This is the first time scanning a QRCode, i.e. Setup
            {
                Log.v(getPackageName(), "Key not stored, first time setup");

                byte[] resultBytes;

                try {
                    resultBytes = result.getBytes("UTF-8");
                    Log.v(getPackageName(), "Result byte array: " + resultBytes);

                    boolean success = ks.put("twofactorkey", resultBytes);

                    if (!success) {
                           int errorCode = ks.getLastError();
                           throw new RuntimeException("Keystore error: " + errorCode); 
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.v(getPackageName(), "Stored in keystore");

                Toast setupComplete = Toast.makeText(getBaseContext(), "You have completed setup", Toast.LENGTH_SHORT);
                setupComplete.show();
            }
        }
    }

}
