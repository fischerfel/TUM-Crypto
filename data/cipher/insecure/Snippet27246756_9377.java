import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import security.SHA256Algo;
import shradhafinalwiddesign.UpdateFile;
import shradhafinalwiddesign.UserRegistration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple TripleDES Encrypt/Decrypt Test 
 * sha1, utf-8, no padding
 *
 * uses commons-codec-1.6 
 * javac -cp :commons-codec-1.6.jar TripleDESTest.java
 * java -cp :commons-codec-1.6.jar TripleDESTest 
 */

public class TripleDesDemo {


    public static void main(String[] args) throws Exception {

        String text = "textToEncrypt";
        UserRegistration user = new UserRegistration() ;
        user.setlUsername("tarunv") ;
        user.setAnswer("tommysdsfdsfsd") ;
        user.setLastaccess("pets namesdfsfds") ;
        user.setLpassword("computersdfdsfd") ;

        String h1 = SHA256Algo.createHash(user.getlUsername()) ;
        String h2 = SHA256Algo.createHash(user.getLpassword()) ;
        String h3 = SHA256Algo.createHash(user.getAnswer()) ;

        String hash1 = UpdateFile.modifyHashValue(h1).substring(0, 24) ;
        String hash2 = UpdateFile.modifyHashValue(h2) ;
        String hash3 = UpdateFile.modifyHashValue(h3) ;

        System.out.println("    key1 : "+hash1.length()+"    key2 : "+hash2.length()+"   key3 : "+hash3.length());
        byte[] arr = toByteArray(user) ;

        byte[] codedtext = TripleDesDemo._encrypt(arr,"tarunvermacdac@gmail.com");
        byte[] codedtext1 = TripleDesDemo._encrypt(codedtext,"tarun.spicyabc@gmail.com");
        byte[] codedtext2 = TripleDesDemo._encrypt(codedtext1,"direct_tarun@yahoo.co.in");

        writeSmallBinaryFile(codedtext2, "tarun.bat") ;
        byte[] texttoDecrypt = readSmallBinaryFile("tarun.bat");

        byte[] decodedtext = TripleDesDemo._decrypt(texttoDecrypt,"direct_tarun@yahoo.co.in");
        byte[] decodedtext1 = TripleDesDemo._decrypt(decodedtext,"tarun.spicyabc@gmail.com");
        byte[] decodedtext2 = TripleDesDemo._decrypt(decodedtext1,"tarunvermacdac@gmail.com");

        System.out.println(codedtext + " ---> " + toObject(decodedtext2));

      }


    public static byte[] _encrypt(byte[] plainTextBytes, String secretKey) throws Exception {

        byte[] keyBytes = secretKey.getBytes();

        SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        //byte[] plainTextBytes = message.getBytes("utf-8");
        byte[] buf = cipher.doFinal(plainTextBytes);
        byte [] base64Bytes = Base64.encodeBase64(buf);
        //String base64EncryptedString = new String(base64Bytes);

        return base64Bytes ;
    }

    public static byte[] _decrypt(byte[] encryptedText, String secretKey) throws Exception {

        //byte[] message = Base64.decodeBase64(encryptedText);
        byte[] message = Base64.decodeBase64(encryptedText);
        byte[] keyBytes = secretKey.getBytes();
        SecretKey key = new SecretKeySpec(keyBytes, "DESede");

        Cipher decipher = Cipher.getInstance("DESede");
        decipher.init(Cipher.DECRYPT_MODE, key);

        byte[] plainText = decipher.doFinal(message);
        return plainText ;
        //return toObject(plainText);
    }

    public static byte[] toByteArray(UserRegistration obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return bytes;
    }

    public static UserRegistration toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        UserRegistration obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = (UserRegistration) ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return obj;
    }

    public static byte[] readSmallBinaryFile(String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        return Files.readAllBytes(path);
    }

    public static void writeSmallBinaryFile(byte[] aBytes, String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        Files.write(path, aBytes); //creates, overwrites
    }
}
