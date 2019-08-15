import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.security.*;
import java.util.Map;

public class AES_retest {

    private static final String plaintext = "Hallo Welt";
    private static final String key = "C0BAE23DF8B51807B3E17D21925FADF2";
    private String iv_string = "I need a initialization vector...";
    private Cipher encrypt_cipher_ctr, decrypt_cipher_ctr, encrypt_cipher_cbc, decrypt_cipher_cbc;

    @Before
    public void prepare_Test() throws GeneralSecurityException {
        byte[] tmp = new byte[16];
        System.arraycopy(iv_string.getBytes(), 0, tmp, 0, 16);

        removeCryptographyRestrictions();

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        //initialization vector
        IvParameterSpec iv = new IvParameterSpec(tmp);
        encrypt_cipher_cbc = Cipher.getInstance("AES/CBC/PKCS5Padding");
        encrypt_cipher_cbc.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
        decrypt_cipher_cbc = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decrypt_cipher_cbc.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
    }

    @Test
    public void multiple_CBC_update() throws BadPaddingException, IllegalBlockSizeException {
        System.out.println("Testing CBC:");
        System.out.println("Plaintext: " + plaintext);
        System.out.println("Plaintext as HEX: " + bytesToHex(plaintext.getBytes()));
        byte[] first_encryption = encrypt_cipher_cbc.update(plaintext.getBytes());
        byte[] second_encryption = encrypt_cipher_cbc.update(plaintext.getBytes());
        encrypt_cipher_cbc.doFinal();
        byte[] first_decryption = decrypt_cipher_cbc.update(first_encryption);
        byte[] second_decryption = decrypt_cipher_cbc.update(second_encryption);
        decrypt_cipher_cbc.doFinal();
        System.out.println("First encryption: " + bytesToHex(first_encryption));
        System.out.println("Second encryption: " + bytesToHex(second_encryption));
        System.out.println("First decryption: " + bytesToHex(first_decryption));
        System.out.println("Second decryption: " + bytesToHex(second_decryption));
    }
}
