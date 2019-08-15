package com.example.aes;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PBEencryptdecryptActivity extends Activity {
    private int IO_BUFFER_SIZE;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        KeyGenerator keygen;
        try {
            keygen = KeyGenerator.getInstance("AES");
            SecretKey aesKey = keygen.generateKey();
            Cipher aesCipher,aesCipherDec;

            AssetManager am = this.getAssets();
            InputStream is = am.open("007FRAMESUPERIOR.jpg"); // get the encrypted image from assets folder
            Log.v("Size","Size of inputstream "+is.available());


            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            byte[] b = new byte[IO_BUFFER_SIZE];  

            int read;  
            while ((read = is.read(b)) != -1) {  //convert inputstream to bytearrayoutputstream
                baos.write(b, 0, read);
            }
            Log.v("Size","Size of b "+b.length);

            aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");     // Create the cipher            
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);    // Initialize the cipher for encryption                         
            byte[] ciphertext = aesCipher.doFinal(b);   // Encrypt the cleartext
            Log.v("Size","Size of image encrypted "+ciphertext.length);


            aesCipherDec =  Cipher.getInstance("AES/ECB/PKCS5Padding");
            aesCipherDec.init(Cipher.DECRYPT_MODE, aesKey); // Initialize the same cipher for decryption                        
            byte[] cleartext1 = aesCipher.doFinal(ciphertext);  // Decrypt the ciphertext                   

            //Bitmap bitmap = BitmapFactory.decodeByteArray(cleartext1 , 0,  cleartext1.length);    //decoding bytearrayoutputstream to bitmap
            Log.v("Size","Size of image decrypted "+cleartext1.length);

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("Error", "Error Occured "+e);
        }

    }
}
