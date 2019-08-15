package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class MyCrypto
{
    SecretKeySpec key;
    Cipher cipher;
    byte[] iv = {0,0,0,0,0,0,0,0};
    IvParameterSpec ivspec = new IvParameterSpec(iv);

    MyCrypto() throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException
    {
        key =new SecretKeySpec("22042016".getBytes(), "DES");
        cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    }

    public void encrypt(File file) throws InvalidKeyException, IOException
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file);
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

         byte[] block = new byte[8];
         int i;
         while ((i = fis.read(block)) != -1) {
         cos.write(block, 0, i);
         }
     cos.close();
     fis.close();
    }

    public void decrypt(File file) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
        FileInputStream fis = new FileInputStream(file);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        FileOutputStream fos = new FileOutputStream(file);

        byte[] block = new byte[8];
         int i;
        while ((i = cis.read(block)) != -1) {
        fos.write(block, 0, i);
        }
        cis.close();
        fos.close();


    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, InvalidAlgorithmParameterException
    {
        MyCrypto crypto = new MyCrypto();
        File cryptoFile = new File(".../crypto.txt");
        crypto.encrypt(cryptoFile);
        crypto.decrypt(cryptoFile);
    }
}
