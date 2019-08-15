package com.android.basetableview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class BaseTableViewActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        ImageView imgView = (ImageView) findViewById(R.id.imgView);

        Bitmap myBitmap = getBitmapFromURL("http://pu-twitter.netau.net/card1.png");
        imgView.setImageBitmap(myBitmap);
    }


    public static Bitmap getBitmapFromURL(String src) {
        Bitmap myBitmap = null;
            try {

                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                //Decryption
                try {
                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
                SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
                IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

                InputStream input = connection.getInputStream();
                CipherInputStream cis = new CipherInputStream(input, cipher);
                FileOutputStream fos  = new FileOutputStream(
                           new File(Environment.getExternalStorageDirectory(), "card2_decrypted.jpg"));
                byte[] b = new byte[8];
                int i;

                while ((i = cis.read(b)) != -1) {
                  fos.write(b, 0, i);
                }
                fos.flush(); fos.close();
                cis.close(); input.close();


                //myBitmap = BitmapFactory.decodeStream(input);

                }
                catch(Exception e){
                    e.fillInStackTrace();
                    Log.v("ERROR","Errorchence : "+e);
                }

                return myBitmap;


            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
}
