package crypting;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.microedition.midlet.*;

public class Encryptor extends MIDlet {

    String buffer;

    public void startApp() {
        String keyString = "testtest";
//        encrypt("Text for encrypting", keyString);
        encrypt("Привет", keyString);
        decrypt(buffer, keyString);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void encrypt(String textToEnrypt, String keyString) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return;
        }

        byte[] keyData = keyString.getBytes();
        SecretKeySpec key = new SecretKeySpec(keyData, 0, keyData.length, "DES");

        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return;
        }

        int cypheredBytes = 0;

        byte[] inputBytes;
        try {
            inputBytes = textToEnrypt.getBytes("UTF-8");
//            inputBytes = textToEnrypt.getBytes();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return;
        }

        byte[] outputBytes = new byte[100];

        try {
            cypheredBytes = cipher.doFinal(inputBytes, 0, inputBytes.length,
                    outputBytes, 0);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return;
        }

        String str = new String(outputBytes, 0, cypheredBytes);
        buffer = str;
        System.out.println("Encrypted string = " + str);
    }


    public void decrypt(String textToDecrypt, String keyString) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return;
        }

        byte[] keyData = keyString.getBytes();
        SecretKeySpec key = new SecretKeySpec(keyData, 0, keyData.length, "DES");

        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception ex) {
            System.out.println("2. " + ex.toString());
            return;
        }

        int cypheredBytes = 0;

        byte[] inputBytes;
        try {
            inputBytes = textToDecrypt.getBytes("UTF-8");
//            inputBytes = textToDecrypt.getBytes();
        } catch (Exception ex) {
            System.out.println("3. " + ex.toString());
            return;
        }

        byte[] outputBytes = new byte[100];

        try {
            cypheredBytes = cipher.doFinal(inputBytes, 0, inputBytes.length,
                    outputBytes, 0);
        } catch (Exception ex) {
            System.out.println("4. " + ex.toString());
            return;
        }

        String str = new String(outputBytes, 0, cypheredBytes);
        System.out.println("Decrypted string = " + str);
    }
}
