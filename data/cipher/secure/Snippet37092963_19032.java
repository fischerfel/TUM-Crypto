import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherEncrypter extends Encrypter<SecretKeySpec> {

    byte[] iv;
    private Cipher cipher;
    private IvParameterSpec ivSpec;



public CipherEncrypter() throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        generateKey();
    }

    public String encrypt(String input) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        return new String(encrypt(input.getBytes(StandardCharsets.UTF_8)));
    }
    public byte[] encrypt(byte[] input) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] iv = new byte[cipher.getBlockSize()];
        new SecureRandom().nextBytes(iv);
        ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encrypted = cipher.doFinal(input);

        return encrypted;
    }

    public String decrypt(String encrypted) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        return new String(decrypt(encrypted.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
    public byte[] decrypt(byte[] encrypted) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        System.out.println("decrypted: " + new String(decrypted, StandardCharsets.UTF_8));

        return decrypted;
    }

    public SecretKeySpec generateKey() throws NoSuchAlgorithmException {
        return generateKey(UUID.randomUUID().toString());
    }
    public SecretKeySpec generateKey(String seed) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(seed.getBytes());
        byte[] keyBytes = new byte[16];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
        key = new SecretKeySpec(keyBytes, "AES");
        return key;
    }

}
