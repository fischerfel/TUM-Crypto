package com.example.crypto;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       Button encryptButton = (Button) findViewById(R.id.button1);
       Button DecryptButton = (Button) findViewById(R.id.button2);


       encryptButton.setOnClickListener(new OnClickListener() {

              @Override
              public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                           encrypt();
                    } catch (InvalidKeyException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                    } catch (IOException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                    }
              }
       });
 public void decryptFile(){

       String inFile = "sample.enc";
       String outFile = "sample.jpg";
       String dir = Environment.getExternalStorageDirectory() +"/Books/";
       InputStream is = null ;

       byte[] filesize = new byte[8];
       byte[] iv = new byte[16];
       try {
           is = new FileInputStream(dir+inFile);

           is.read(filesize);
           is.read(iv);

       } catch (FileNotFoundException e1) {
           // TODO Auto-generated catch block
           Log.d("D1","no file found");
       } catch (IOException e) {
           // TODO Auto-generated catch block
           Log.d("D-2","no file found");
           e.printStackTrace();
       }

       byte[] k = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

       Key key = new SecretKeySpec(k,"AES");




       try {
           Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
           cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(iv));
           OutputStream outs = new FileOutputStream(dir+outFile);
           //is = new FileInputStream(dir+inFile);

           while(true){
               byte[] chunk = new byte[64*1024];

               is.read(chunk);
               if(chunk.length == 0){

                   break;

               }
               outs.write(cipher.doFinal(chunk));              
           }


       } catch (NoSuchAlgorithmException e) {
           // TODO Auto-generated catch block
           Log.d("D","1");

           e.printStackTrace();
       } catch (NoSuchPaddingException e) {
           // TODO Auto-generated catch block
           Log.d("D","2");
           e.printStackTrace();
       } catch (InvalidKeyException e) {
           // TODO Auto-generated catch block
           Log.d("D","3");
           e.printStackTrace();
       } catch (InvalidAlgorithmParameterException e) {
           // TODO Auto-generated catch block
           Log.d("D","4");
           e.printStackTrace();
       } catch (FileNotFoundException e) {
           // TODO Auto-generated catch block
           Log.d("D","5");
           e.printStackTrace();
       } catch (IOException e) {
           // TODO Auto-generated catch block
           Log.d("D","6");
           e.printStackTrace();
       } catch (IllegalBlockSizeException e) {
           // TODO Auto-generated catch block
           Log.d("D","7");
           e.printStackTrace();
       } catch (BadPaddingException e) {
           // TODO Auto-generated catch block
           Log.d("D","8");
           e.printStackTrace();
       }


   }
