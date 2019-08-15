package get;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class CipherExample {

    public static void main(String[] args) {
        try {
            String key = "squirrel123"; // needs to be at least 8 characters for DES

            FileInputStream fis = new FileInputStream("/home/anand/Desktop/encrypt/c/cute.jpg");
            FileOutputStream fos = new FileOutputStream("/home/anand/Desktop/encrypt/c/encrypted.jpg");
            encrypt(key, fis, fos);

            FileInputStream fis2 = new FileInputStream("/home/anand/Desktop/encrypt/c/encrypted.jpg");
            FileOutputStream fos2 = new FileOutputStream("/home/anand/Desktop/encrypt/c/decrypted.jpg");
            decrypt(key, fis2, fos2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
    }

    public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
    }

    public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            doCopy(cis, os);
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, desKey);

            // PipedInputStream pis = new PipedInputStream();
               // PipedOutputStream pos = new PipedOutputStream(pis);

            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            //doCopying(is);

            doCopy(is, os);
        }
    }

    public static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();

    }


    }
