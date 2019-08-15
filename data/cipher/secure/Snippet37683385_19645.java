package szyfrator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.tools.bzip2.CBZip2OutputStream;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class Cryptography {

    private static byte[] aesKey;
    private static String base64AESKey;
    private static byte[] encryptedAESKey;
    private static String base64AESEncryptedKey;
    private static byte[] aesKeyTransformed;

    public static void main(String args[]){

        Cryptography.generateAESkey();
        Cryptography.encryptAESKey(new File("G:\\HASHBABYHASH\\public.txt"));
        Cryptography.decryptAESKey(new File("G:\\HASHBABYHASH\\private.txt"));

        System.out.println("String: " + Base64.encode(Cryptography.getAesKey()) + "\r\n");
        System.out.println("Encrypted string: " + Cryptography.getBase64EncryptedKey() + "\r\n");
        System.out.println("Decrypted String: " + Base64.encode(Cryptography.getAesKeyTransformed()) + "\r\n");

    }

    public static void generateAESkey(){

        try {
            KeyGenerator    keyGen = KeyGenerator.getInstance("AES");

            keyGen.init(256); 
            SecretKey secretKey = keyGen.generateKey();

            byte[] keyBytes = secretKey.getEncoded(); 
            base64AESKey = Base64.encode(keyBytes); 

            aesKey = keyBytes;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void encryptAESKey(File publicKeyFile){

        try {       
            FileInputStream input = new FileInputStream(publicKeyFile);

            byte[] decoded = Base64.decode(IOUtils.toByteArray(input));     

            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicSpec);   

            Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);  

            encryptedAESKey = cipher.doFinal(aesKey);
            base64AESEncryptedKey = Base64.encode(encryptedAESKey);

            input.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decryptAESKey(File privateKeyFile){

        try {
            FileInputStream input = new FileInputStream(privateKeyFile);

            byte[] decoded = Base64.decode(IOUtils.toByteArray(input));

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);  

            aesKeyTransformed = cipher.doFinal(encryptedAESKey);
            input.close();  
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
