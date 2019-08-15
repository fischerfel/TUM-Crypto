import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESWithStaticKeyAndRandomIV {

    private static byte[] KEY = new byte[] { (byte) 0x14, (byte) 0x0b,
            (byte) 0x41, (byte) 0xb2, (byte) 0x2a, (byte) 0x29, (byte) 0xbe,
            (byte) 0xb4, (byte) 0x06, (byte) 0x1b, (byte) 0xda, (byte) 0x66,
            (byte) 0xb6, (byte) 0x74, (byte) 0x7e, (byte) 0x14 };

    public static byte[] encrypt(byte[] plaintext) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey key = new SecretKeySpec(KEY, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

            byte[] ciphertext = new byte[iv.length
                    + cipher.getOutputSize(plaintext.length)];
            System.arraycopy(iv, 0, ciphertext, 0, iv.length);
            cipher.doFinal(plaintext, 0, plaintext.length, ciphertext,
                    iv.length);
            return ciphertext;
        } catch (InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidParameterSpecException
                | ShortBufferException | IllegalBlockSizeException
                | BadPaddingException e) {
            throw new IllegalStateException(
                    "CBC encryption with standard algorithm should never fail",
                    e);
        }
    }

    public static byte[] decrypt(byte[] ciphertext) throws IllegalBlockSizeException,
            BadPaddingException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(KEY, "AES");

            if (ciphertext.length < cipher.getBlockSize()) {
                throw new IllegalArgumentException(
                        "Ciphertext too small to contain IV");
            }

            IvParameterSpec ivSpec = new IvParameterSpec(ciphertext, 0,
                    cipher.getBlockSize());
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

            byte[] plaintext = new byte[cipher.getOutputSize(ciphertext.length
                    - cipher.getBlockSize())];
            cipher.doFinal(ciphertext, cipher.getBlockSize(), ciphertext.length
                    - cipher.getBlockSize(), plaintext, 0);
            return plaintext;
        } catch (InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException | ShortBufferException
                | InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(
                    "CBC decryption with standard algorithm should be available",
                    e);
        }
    }

    public static void main(String[] args) throws Exception {
           byte[] plaintext = decrypt(encrypt("owlstead".getBytes(StandardCharsets.UTF_8)));
           System.out.println(new String(plaintext, StandardCharsets.UTF_8));
    }
}
