package com.android.decrypt;

import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

    public class DecryptPDFActivity extends Activity {

        @Override
        public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        //FileInputStream fis   = new FileInputStream(new File("encrypted.pdf"));
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        FileOutputStream fos  = new FileOutputStream(new File("decrypted.pdf"));

        byte[] b = new byte[8];
        int i;

        while ((i = cis.read(b)) != -1) {
          fos.write(b, 0, i);
        }
        fos.flush(); fos.close();
        cis.close(); fis.close();

        }
        catch(Exception e)
        {
            e.fillInStackTrace();
            Log.e("Error", "Damned ! : "+e);
        }
      }
}
