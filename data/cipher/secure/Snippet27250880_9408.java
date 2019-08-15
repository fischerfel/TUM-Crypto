package com.example.videoplay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class Encrypter { 
Context contxt;
File inFile;
File outFile;
File outFile_dec;
SecretKey key;
byte[] keyData;
SecretKey key2;
byte[] iv;
AlgorithmParameterSpec paramSpec;

private final static int IV_LENGTH = 16; // Default length with Default 128
                                            // key AES encryption 
private final static int DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE = 1024;

private final static String ALGO_RANDOM_NUM_GENERATOR = "SHA1PRNG";
private final static String ALGO_SECRET_KEY_GENERATOR = "AES";
private final static String ALGO_VIDEO_ENCRYPTOR = "AES/CBC/PKCS5Padding";

public static String str_key,str_paramSpec,str_key2;

@SuppressWarnings("resource") 
public static void encrypt(SecretKey key, AlgorithmParameterSpec paramSpec, InputStream in, OutputStream out)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
        InvalidAlgorithmParameterException, IOException {
    try { 
         byte[] iv = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 
         0x07, 0x72, 0x6F, 0x5A, (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 
         0x07, 0x72, 0x6F, 0x5A }; 
         paramSpec = new IvParameterSpec(iv); 
        Cipher c = Cipher.getInstance(ALGO_VIDEO_ENCRYPTOR);
        c.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        out = new CipherOutputStream(out, c);
        int count = 0;
        byte[] buffer = new byte[DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE];
        while ((count = in.read(buffer)) >= 0) {
            out.write(buffer, 0, count);
        } 
    } finally { 
        out.close();
    } 
} 

@SuppressWarnings("resource") 
public static void decrypt(SecretKey key, AlgorithmParameterSpec paramSpec, InputStream in, OutputStream out)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
        InvalidAlgorithmParameterException, IOException {
    try { 
         byte[] iv = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 
         0x07, 0x72, 0x6F, 0x5A, (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 
         0x07, 0x72, 0x6F, 0x5A }; 
         paramSpec = new IvParameterSpec(iv); 
        Cipher c = Cipher.getInstance(ALGO_VIDEO_ENCRYPTOR);
        c.init(Cipher.DECRYPT_MODE, key, paramSpec);
        out = new CipherOutputStream(out, c);
        int count = 0;
        byte[] buffer = new byte[DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE];
        while ((count = in.read(buffer)) >= 0) {
            out.write(buffer, 0, count);
        } 
    } finally { 
        out.close();
    } 
} 

    public Encrypter(Context ctx) {
    //      
    inFile = new File(Environment.getExternalStorageDirectory().getPath() +MainActivity.Directory+MainActivity.filename );
    outFile = new File(Environment.getExternalStorageDirectory().getPath() + MainActivity.Directory+"/"+MainActivity.temponlyfilename);
    outFile_dec = new File(Environment.getExternalStorageDirectory().getPath() + MainActivity.Directory+MainActivity.filename);

    contxt=ctx;
    try { 

        //........................................................................................

        SharedPreferences  mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = mPrefs.getString("pref_str_key", "empty");
        if(json.equalsIgnoreCase("empty"))
        {
            key = KeyGenerator.getInstance(ALGO_SECRET_KEY_GENERATOR).generateKey();

            keyData = key.getEncoded();
            key2 = new SecretKeySpec(keyData, 0, keyData.length, ALGO_SECRET_KEY_GENERATOR); //if you want to store key bytes to db so its just how to //recreate back key from bytes array

            iv = new byte[IV_LENGTH];
            SecureRandom.getInstance(ALGO_RANDOM_NUM_GENERATOR).nextBytes(iv); // If
                                                                                // storing 
                                                                                // separately 
            paramSpec = new IvParameterSpec(iv);

            Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            str_key = gson.toJson(key);
            prefsEditor.putString("pref_str_key", str_key);
            str_key2 = gson.toJson(2);
            prefsEditor.putString("pref_str_key2", str_key2);
            prefsEditor.commit();

        }else
        {
            Gson gson = new Gson();
            str_key = mPrefs.getString("pref_str_key", "");
            key = gson.fromJson(str_key, SecretKey.class);
            str_key2 = mPrefs.getString("pref_str_key2", "");
            key2 = gson.fromJson(str_key2, SecretKey.class);
        }
        //........................................................................................

    } catch (Exception e) {
        e.printStackTrace();
    } 

}

public File Decrypt(File dir)
{
    File dirout = new File(dir.getParent()+"/temp.ts");
try {
    Encrypter.decrypt(key2, paramSpec, new FileInputStream(dir), new FileOutputStream(dirout));
    outFile_dec.delete();
} catch (Exception e) {
    e.printStackTrace();
} 
return dirout;
}

public File Encrypt(File dir) {
    File dirout = new File(dir.getParent()+"/temp.ts");
    // TODO Auto-generated method stub
    try {
        Encrypter.encrypt(key, paramSpec, new FileInputStream(dir), new FileOutputStream(dirout));
        dir.delete();
    } catch (Exception e) {
        e.printStackTrace();
    } 
    return dirout;
}




public void DecryptFromTo(File dir,File dirout)
{

try {
    Encrypter.decrypt(key2, paramSpec, new FileInputStream(dir), new FileOutputStream(dirout));

} catch (Exception e) {
    e.printStackTrace();
} 

}



} 
