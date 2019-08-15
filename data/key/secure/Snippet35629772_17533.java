import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
class Encryptiontest {
    private static void log(String u) { System.out.println(u); }
    public static void main(String[] args) {
        final String IV =       "0123456789ABCDEF"; //16-byte
        final String KEY =      "AnEncryptionKey1"; //16-byte, works fine!

        //results in java.security.InvalidKeyException: Illegal key size exception
        //final String KEY =      "AnEncryptionKey1AnEncryptionKey1"; //32-byte

        //generate a bunch of random characters to encrypt
        StringBuilder sb = new StringBuilder();
        sb.append("THIS IS THE START OF THE STRING|");
        for (int i = 0; i < 263; i++)
            sb.append((char)((int)(Math.random() * (126 - 33 + 1)) + 33));
        sb.append("|THIS IS THE END OF THE STRING");
        final String PLAIN_TEXT = sb.toString();
        log("Plain Text To Encrypt:");
        log(PLAIN_TEXT);
        log("No. characters: " + PLAIN_TEXT.length());

        log("Encrypting data using AES symmetric block cipher . . . ");
        byte[] bResult = encrypt(KEY, IV, PLAIN_TEXT);

        log("Saving encrypted data to file . . .");
        File f = new File("test_Encrypted.TXT");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            if (f.exists())
                f.delete();
            else
                f.createNewFile();
            fos.write(bResult);
            fos.flush();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Encryptiontest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Encryptiontest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {
                Logger.getLogger(Encryptiontest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.exit(1);
    }

    static byte[] encrypt (String key, String iv, String plainText) {
        byte[] encrypted = null;
        try {
            IvParameterSpec IV = new IvParameterSpec(iv.getBytes());
            SecretKeySpec skeyspec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeyspec, IV);
            encrypted = cipher.doFinal(plainText.getBytes());
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                InvalidAlgorithmParameterException |
                IllegalBlockSizeException |
                BadPaddingException ex) {
            Logger.getLogger(Encryptiontest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encrypted;
    }
}
