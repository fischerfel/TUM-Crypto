package com.example.secretpictures;
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

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class EncryptService extends Service {
private static final String TAG = EncryptService.class.getSimpleName();
@Override
public IBinder onBind(Intent intent) {
    return null;



}
@Override
public void onCreate() {
    // TODO Auto-generated method stub
    super.onCreate();

Log.d(TAG, "OnCreate");
}


@SuppressLint("SdCardPath")
@Override
public void onStart(Intent intent, int startId) {
    // TODO Auto-generated method stub
    super.onStart(intent, startId);

Log.d(TAG, "OnStart");

File file[] = Environment.getExternalStorageDirectory().listFiles();
recursiveFileFind(file);


try {

FileInputStream fis = new FileInputStream("/mnt/sdcard/secretpictures/yolo.png");
// This stream write the encrypted text. This stream will be wrapped by another stream.
FileOutputStream fos = new FileOutputStream("/mnt/sdcard/secretpictures/yolo2.enc");

// Length is 16 byte
SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
// Create cipher
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks);
    // Wrap the output stream
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);
    // Write bytes
    int b;
    byte[] d = new byte[8];
    while((b = fis.read(d)) != -1) {
        cos.write(d, 0, b);
    }
    // Flush and close streams.
    cos.flush();
    cos.close();
    fis.close();


} catch (IOException  e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

}

@Override
public void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();

Log.d(TAG, "OnDestroy");
}



public void recursiveFileFind(File[] file1){
int i = 0;
String filePath="";
 if(file1!=null){
while(i!=file1.length){
    filePath = file1[i].getAbsolutePath();
        if(file1[i].isDirectory()){
                File file[] = file1[i].listFiles();
            recursiveFileFind(file);
            }
        i++;
        Log.d(i+"", filePath);

}
}
}   
}
