import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

public class MainNew {

    public static void main(String[] args) {
        String iv = getEncryptionIV();
        System.out.println(" iv = "+iv);

        String encryptedData= encryptWithIVandKey(iv,encryptionKey,"rakesh.test@eltropy.com");
        System.out.println(encryptedData);
        String decryptedData = decrypt (iv,encryptionKey,encryptedData);
        System.out.println(decryptedData);
    }


    static final String encryptionKey = "rakesh1@n1111111";


    static byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(encryptMode, key, new IvParameterSpec(DatatypeConverter.parseHexBinary(iv)));
            byte[] data = cipher.doFinal(bytes);

            return data;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;

    }



    static SecretKey generateKey(String passphrase) {

        SecretKey key = null;

        try {

            key = new SecretKeySpec(passphrase.getBytes("UTF-8"), "AES");


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return key;
    }




    static String getEncryptionIV() {
        SecureRandom random = new SecureRandom();
        byte[] ivBytes = new byte[16];
        random.nextBytes(ivBytes);
        return DatatypeConverter.printHexBinary(ivBytes);
    }

    static String encryptWithIVandKey( String iv, String passphrase, final String strToEncrypt) {
        String encryptedStr = "";

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey key = generateKey(passphrase);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(DatatypeConverter.parseHexBinary(iv)));

            encryptedStr = DatatypeConverter.printBase64Binary(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }


        return encryptedStr;
    }

    static String decrypt(String iv, String passphrase, String ciphertext) {
        try {
            SecretKey key = generateKey(passphrase);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, DatatypeConverter.parseBase64Binary(ciphertext));
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return "";
    }

}
