import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Trial {

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

    public static byte[] encryptPdfFile(SecretKey secretKey, byte[] content) {
        Cipher cipher;
        byte[] encrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            encrypted = Base64.encodeBase64(cipher.doFinal(content));

        } catch (Exception e) {

            System.out.println("Error while encrypting: " + e.toString());
        }
        return encrypted;

    }

    public static byte[] decryptPdfFile(SecretKey secretKey, byte[] textCryp) {
        Cipher cipher;
        byte[] decrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decrypted = cipher.doFinal(Base64.decodeBase64(textCryp));

        } catch (Exception e) {

            System.out.println("Error while decrypting: " + e.toString());
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

        SecretKeySpec secretKey;
        byte[] key;
        String myKey = "ThisIsAStrongPasswordForEncryptionAndDecryption";

        MessageDigest sha = null;
        key = myKey.getBytes("UTF-8");
        System.out.println(key.length);
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        System.out.println(key.length);
        System.out.println(new String(key, "UTF-8"));
        secretKey = new SecretKeySpec(key, "AES");

        byte[] content = getFile();
        System.out.println(content);

        byte[] encrypted = encryptPdfFile(secretKey, content);
        System.out.println(encrypted);

        byte[] decrypted = decryptPdfFile(secretKey, encrypted);
        System.out.println(decrypted);

        saveFile(decrypted);
        System.out.println("Done");

    }

}
