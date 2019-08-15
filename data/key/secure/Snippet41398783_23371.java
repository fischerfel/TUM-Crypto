package demo.encryptionanddecryption;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    AppPermissions permissions;
    private static final int PERMISSION_ALL = 1;
    private static final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    byte[] key, iv;
    Button encryption,decryption;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        encryption = (Button)findViewById(R.id.encryption);
        decryption = (Button)findViewById(R.id.decryption);

        permissions = new AppPermissions();
        if (!permissions.hasPermissions(MainActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
            Log.i("logCat", "Permission Granted");
        } else {
            Log.i("logCat", "Permission Rejected");
        }

        // Get key
        key = getKey();

        // Get IV
        iv = getIV();

        encryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //File file = new File(Environment.getExternalStorageDirectory() + File.separator + "test", "test.txt");
                try {
                    encryptFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        decryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    decryptFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void encryptFile(){
        Bitmap bitmap= BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory()+ File.separator + "img.png").toString());
        // Write image data to ByteArrayOutputStream
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100,baos);
        // Encrypt and save the image
        saveFile(encrypt(key,baos.toByteArray()),"enimg.png");
    }


    public void decryptFile(){
        try {
            // Create FileInputStream to read from the encrypted image file
            FileInputStream fis = new FileInputStream(new File(Environment.getExternalStorageDirectory() + File.separator + "enimg.png").toString());
            // Save the decrypted image
            saveFile(decrypt(key, fis),"deimg.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void saveFile(byte[] data, String outFileName){
        FileOutputStream fos=null;

        try {
            fos=new FileOutputStream(new File(Environment.getExternalStorageDirectory() + File.separator + outFileName).toString());
            fos.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally{

            try {
                fos.close();
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
    }


    private byte[] encrypt(byte[] skey, byte[] data){

        SecretKeySpec skeySpec = new SecretKeySpec(skey, "AES");
        Cipher cipher;
        byte[] encrypted=null;

        try {
            // Get Cipher instance for AES algorithm
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // Initialize cipher
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            // Encrypt the image byte data
            encrypted = cipher.doFinal(data);
        }catch(Exception e){
            e.printStackTrace();
        }
        return encrypted;
    }



    private byte[] decrypt(byte[] skey, FileInputStream fis){

        SecretKeySpec skeySpec = new SecretKeySpec(skey, "AES");
        Cipher cipher;
        byte[] decryptedData=null;
        CipherInputStream cis=null;

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));
            // Create CipherInputStream to read and decrypt the image data
            cis = new CipherInputStream(fis, cipher);
            // Write encrypted image data to ByteArrayOutputStream
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[2048];

            while ((cis.read(data)) != -1) {
                buffer.write(data);
            }

            buffer.flush();
            decryptedData=buffer.toByteArray();
        }catch(Exception e){
            e.printStackTrace();
        }

        finally{

            try {
                fis.close();
                cis.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return decryptedData;
    }


    private static byte[]  getKey(){
        KeyGenerator keyGen;
        byte[] dataKey=null;

        try {
            // Generate 256-bit key
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();
            dataKey=secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return dataKey;
    }



    private static byte[] getIV(){

        SecureRandom random = new SecureRandom();
        byte[] iv = random.generateSeed(16);
        return iv;
    }
}
