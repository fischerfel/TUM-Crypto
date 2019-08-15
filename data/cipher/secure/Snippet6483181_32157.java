import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;

public class EncryptAndDecrypt {

    public static Cipher createCipher () throws Exception{

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            return cipher;
    }
    public static KeyPair generateKey () throws  NoSuchAlgorithmException{

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance ("RSA");
            keyGen.initialize(1024);
            KeyPair key = keyGen.generateKeyPair();

            return key;
    }
    public static byte [] encrypt (String  str, Cipher cip, KeyPair key) {

        byte [] cipherText = null;
        try {

            byte [] plainText = str.getBytes("UTF8");
            cip.init(Cipher.ENCRYPT_MODE, key.getPublic());
            cipherText = cip.doFinal(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }
    public static String decrypt (byte [] c, Cipher cip, KeyPair key) throws Exception {

        cip.init(Cipher.DECRYPT_MODE, key.getPrivate());

        byte [] decryptedPlainText = cip.doFinal (c);// exception occurred here
        String decryptedPlainStr = new String (decryptedPlainText);

        return decryptedPlainStr;
    }
}


//separate class below to use the encrypt method

public class EncryptionApp {

    public static void main (String [] args) {

        getEncrypted();
    }
    public static byte [] getEncrypted () {

        byte [] encyptedByte = null;
        try {
            String plainText = "der";
            Cipher cip = Safety.createCipher();
            KeyPair key = Safety.generateKey();
            encyptedByte = Safety.useRSA(plainText, cip, key);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

         return encyptedByte;
    }
}

// Another class to use the decrypt method 

public class DecryptionApp {

    public static void main(String[] args) {
        System.out.println (useDecrypted () );
    }
    public static byte[] useDecrypted () {

        byte [] decryptedText = null;
        try {
            Cipher cip = EncryptAndDecrypt.createCipher();
            KeyPair key = EncryptAndDecrypt.generateKey();
            decryptedText = EncryptAndDecrypt.decrypt(EncryptionApp.getEncrypted(),cip,key);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    return decryptedText;
    }
}
