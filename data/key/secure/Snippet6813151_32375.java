    package com.cryptooo.lol;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class SimpleCryptoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        new Thread() {
            public void run(){
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.shit);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();  
                bm.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is the bitmap object   
                byte[] b = baos.toByteArray();  

                try {
                byte[] keyStart = "MARTIN_123_MARTIN_123".getBytes();
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                sr.setSeed(keyStart);
                kgen.init(128, sr); // 192 and 256 bits may not be available
                SecretKey skey = kgen.generateKey();
                byte[] key = skey.getEncoded();    

                // encrypt
                byte[] encryptedData = encrypt(key,b);
                // decrypt
                long start = System.currentTimeMillis()/1000L;
                byte[] decryptedData = decrypt(key,encryptedData);
                long end = System.currentTimeMillis()/1000L;
                android.util.Log.d("TEST","Time "+ String.valueOf(end-start));
                }
                catch(Exception e){
                    e.fillInStackTrace();
                }
            }

            private byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                byte[] encrypted = cipher.doFinal(clear);
                return encrypted;
            }

            private byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                        byte[] decrypted = cipher.doFinal(encrypted);
            return decrypted;
            }
        }.start(); 
    } 

}
