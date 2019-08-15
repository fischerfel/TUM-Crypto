 import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class AESDecrypt {
    private static final byte[] SALT = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03};
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;
    private static final int IV_LENGTH = 16;
    private Cipher eCipher;
    private Cipher dCipher;
    private byte[] encrypt;

    AESDecrypt(String passPhrase, String encryptedString) throws Exception {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
        SecretKey secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
        SecretKey secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");

        encrypt = Base64.decodeBase64(encryptedString);

        eCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        eCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] iv = extractIV();
        dCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        dCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
    }


    private byte[] extractIV() {
        byte[] iv = new byte[IV_LENGTH];
        System.arraycopy(encrypt, 0, iv, 0, iv.length);
        return iv;
    }

    public String decrypt() throws Exception {
        byte[] bytes = extractCipherText();

        byte[] decrypted = decrypt(bytes);
        return new String(decrypted, "UTF8");
    }

    private byte[] extractCipherText() {
        byte[] ciphertext = new byte[encrypt.length - IV_LENGTH];
        System.arraycopy(encrypt, 16, ciphertext, 0, ciphertext.length);
        return ciphertext;
    }

    public byte[] decrypt(byte[] encrypt) throws Exception {
        return dCipher.doFinal(encrypt);
    }

    public static void main(String[] args) throws Exception {
        String passphrase = "PASSWORDPASSPHRASE";
        String encryptedPassword = "LiN1KmaB2Pl7cooe3qvuImbAKXsVJt5oxt+ajuVZ5n4=";        
        AESDecrypt aesDecrypt = new AESDecrypt(passphrase, encryptedPassword);
        String decryptedPassword = aesDecrypt.decrypt();
        System.out.println("decryptedPasswpord = " + decryptedPassword);
    }
}
