import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class ImageEncDec {

    public static byte[] getFile() {

        File f = new File("/home/bridgeit/Desktop/Olympics.jpg");
        InputStream is = null;
        try {
            is = new FileInputStream(f);
        } catch (FileNotFoundException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        byte[] content = null;
        try {
            content = new byte[is.available()];
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            is.read(content);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return content;
    }

    public static byte[] encryptPdfFile(Key key, byte[] content) {
        Cipher cipher;
        byte[] encrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypted = cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;

    }

    public static byte[] decryptPdfFile(Key key, byte[] textCryp) {
        Cipher cipher;
        byte[] decrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypted = cipher.doFinal(textCryp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted;
    }

    public static void saveFile(byte[] bytes) throws IOException {

        FileOutputStream fos = new FileOutputStream("/home/bridgeit/Desktop/Olympics-new.jpg");
        fos.write(bytes);
        fos.close();

    }

    public static void main(String args[])
            throws NoSuchAlgorithmException, InstantiationException, IllegalAccessException, IOException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();
        System.out.println(key);

        byte[] content = getFile();
        System.out.println(content);

        byte[] encrypted = encryptPdfFile(key, content);
        System.out.println(encrypted);

        byte[] decrypted = decryptPdfFile(key, encrypted);
        System.out.println(decrypted);

        saveFile(decrypted);
        System.out.println("Done");

    }

}
