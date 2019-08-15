package com.example.rama.beta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button encryptButton = (Button) findViewById(R.id.button1);
        Button DecryptButton = (Button) findViewById(R.id.button2);
        encryptButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    encrypt();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        DecryptButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    decrypt();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    static void encrypt() throws IOException, NoSuchAlgorithmException,
                NoSuchPaddingException, InvalidKeyException {
            File extStore = Environment.getExternalStorageDirectory();
            FileInputStream fis = new FileInputStream(extStore + "/abc.m4v");
            FileOutputStream fos = new FileOutputStream(extStore + "/encabc.m4v");

            SecretKeySpec sks = new SecretKeySpec("xxxx".getBytes(),
                    "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks);

            CipherOutputStream cos = new CipherOutputStream(fos, cipher);

            int read;
            byte[] buffer = new byte[1024];
            while ((read = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, read);
            }

            cos.flush();
            cos.close();
            fis.close();
        }

    static void decrypt() throws IOException, NoSuchAlgorithmException,
                NoSuchPaddingException, InvalidKeyException {

            File extStore = Environment.getExternalStorageDirectory();
            FileInputStream fis = new FileInputStream(extStore + "/encabc.m4v");

            FileOutputStream fos = new FileOutputStream(extStore + "/decabc.m4v");
            SecretKeySpec sks = new SecretKeySpec("xxxx".getBytes(),
                    "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            int read;
            byte[] buffer = new byte[1024];
            while ((read = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            fos.flush();
            fos.close();
            cis.close();

        }
    }
