import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

public class FileActivity extends Activity {

    private String encryptedFileName = "sample.pdf.aes";//
    private static String algorithm = "AES";
    static SecretKey yourKey = null;
     TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
       // saveFile("Hi friends");
        try {

            saveFile(  new String(loadFile(Environment.getExternalStorageDirectory()+"/pdf.pdf")));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        text=(TextView)findViewById(R.id.text);
           text.setText(decodeFile());

    }
    public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Number of PBKDF2 hardening rounds to use. Larger values increase
        // computation time. You should select a value that causes computation
        // to take >100ms.
        final int iterations = 1000;

        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecretKeyFactory secretKeyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations,
                outputKeyLength);
        yourKey = secretKeyFactory.generateSecret(keySpec);
        return yourKey;
    }

    public static SecretKey generateSalt() throws NoSuchAlgorithmException {
        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecureRandom secureRandom = new SecureRandom();
        // Do *not* seed secureRandom! Automatically seeded from system entropy.
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static byte[] encodeFile(SecretKey yourKey, byte[] fileData)
            throws Exception {
        byte[] data = yourKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length,
                algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(fileData);

        return encrypted;
    }

    public static byte[] decodeFile(SecretKey yourKey, byte[] fileData)
            throws Exception {
        byte[] data = yourKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length,
                algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] decrypted = cipher.doFinal(fileData);

        return decrypted;
    }

    void saveFile(String stringToSave) {
        try {
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator, encryptedFileName);
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            char[] p = { 'p', 'a', 's', 's' };
            SecretKey yourKey = generateKey(p, generateSalt().toString()
                    .getBytes());
            byte[] filesBytes = encodeFile(yourKey, stringToSave.getBytes());
            bos.write(filesBytes);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static byte[] loadFile(String sourcePath) throws IOException
    {
        InputStream inputStream = null;
        try 
        {
            inputStream = new FileInputStream(sourcePath);
            return readFully(inputStream);
        } 
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
    }
    public static byte[] readFully(InputStream stream) throws IOException
    {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1)
        {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }
    String decodeFile() {
        String str = null;
        try {
            byte[] decodedData = decodeFile(yourKey,loadFile(Environment.getExternalStorageDirectory()+"/sample.pdf.aes"));
             str = new String(decodedData);
            System.out.println("DECODED FILE CONTENTS : " + str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
 }
