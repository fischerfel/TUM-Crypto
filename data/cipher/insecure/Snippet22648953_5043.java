import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;

import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;





import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.DESKeySpec;

public class MyCiphers {

    public static void main(String[] args) {
        try {



            BufferedReader br = new BufferedReader(new FileReader("key.txt"));
            String key = br.readLine();
            br.close();
            FileInputStream fis = new FileInputStream("original.txt");
            FileOutputStream fos = new FileOutputStream("encrypted.txt");
            encrypt(key, fis, fos);

            FileInputStream fis2 = new FileInputStream("encrypted.txt");
            FileOutputStream fos2 = new FileOutputStream("decrypted.txt");
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


      SecretKeySpec dks = new SecretKeySpec(key.getBytes(),"AES");
        Cipher cipher = Cipher.getInstance("AES"); 

        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, dks);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            doCopy(cis, os);        
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, dks);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            doCopy(is, cos);
        }


    }

    public static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[128];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }

}
