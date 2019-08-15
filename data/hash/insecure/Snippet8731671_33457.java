import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

public class Crypto {
    Cipher ecipher;
    Cipher dcipher;

    /**
    * Input a string that will be md5 hashed to create the key.
    * @return void, cipher initialized
    */

    public Crypto(){
        try{
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            this.setupCrypto(kgen.generateKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Crypto(String key){
        SecretKeySpec skey = new SecretKeySpec(getMD5(key), "AES");
        this.setupCrypto(skey);
    }

    private void setupCrypto(SecretKey key){
        try
        {    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            ecipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            dcipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Buffer used to transport the bytes from one stream to another
    byte[] buf = new byte[1024];

    public void encrypt(InputStream in, OutputStream out){
        try {
            // Bytes written to out will be encrypted
            out = new CipherOutputStream(out, ecipher);

            // Read in the cleartext bytes and write to out to encrypt
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0){
                out.write(buf, 0, numRead);
            }
            out.close();
        }
        catch (java.io.IOException e){
            e.printStackTrace();
        }
    }


    public void decrypt(InputStream in, OutputStream out){

        try {
            // Bytes read from in will be decrypted
            in = new CipherInputStream(in, dcipher);

            // Read in the decrypted bytes and write the cleartext to out
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0) {
                out.write(buf, 0, numRead);
            }
            out.close();
        } catch (java.io.IOException e) {
             e.printStackTrace();
        }
    }

    private static byte[] getMD5(String input){
        try{
            byte[] bytesOfMessage = input.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(bytesOfMessage);
        }  catch (Exception e){
             return null;
        }
    }


    public static void main(String args[]){
        try {


            Crypto encrypter = new Crypto("yursxjdlbkuikeqe");  ///key for decryption logic
             encrypter.encrypt(new FileInputStream("D:\\Path\\Lighthouse.jpg"),new FileOutputStream("D:\\Encryption\\iOS code base\\Lighthouse.jpg.pkcs5"));
               encrypter.decrypt(new FileInputStream("D:\\Path\\Lighthouse.jpg.pkcs5"),new FileOutputStream("D:\\Encryption\\iOS code base\\Lighthouse.jpg"));
             System.out.println("DONE");
       }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
