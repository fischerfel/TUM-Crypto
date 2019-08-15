  import java.io.IOException;
    import java.security.InvalidAlgorithmParameterException;
    import java.security.InvalidKeyException;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;

    import javax.crypto.BadPaddingException;
    import javax.crypto.Cipher;
    import javax.crypto.IllegalBlockSizeException;
    import javax.crypto.NoSuchPaddingException;
    import javax.crypto.SecretKey;
    import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    public static final String TAG = Crypto.class.getSimpleName();
    // Replace me with a 16-byte key, share between Java and C#
    private static Cipher aesCipher;
    private static SecretKey secretKey;
    private static IvParameterSpec ivParameterSpec;
    private static String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static String CIPHER_ALGORITHM = "AES";
    private static byte[] rawSecretKey = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    //    private static String MESSAGEDIGEST_ALGORITHM = "MD5";
    private static String MESSAGEDIGEST_ALGORITHM = "SHA-256";

    public Crypto(String passphrase) {
        byte[] passwordKey = encodeDigest(passphrase);

        try {
            aesCipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such algorithm " + CIPHER_ALGORITHM, e);
        } catch (NoSuchPaddingException e) {
            Log.e(TAG, "No such padding PKCS5", e);
        }

        secretKey = new SecretKeySpec(passwordKey, CIPHER_ALGORITHM);
        ivParameterSpec = new IvParameterSpec(rawSecretKey);
    }

    public byte[] decrypt(byte[] clearData) {
        try {
            aesCipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        byte[] decryptedData;

        try {
            decryptedData = aesCipher.doFinal(clearData);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
        return decryptedData;

    }

    public String decryptAsBase64(byte[] clearData) throws IOException {
        byte[] decryptedData = decrypt(clearData);
        return new String(Base64New.decode(decryptedData));
    }

    public String encryptAsBase64(byte[] clearData) {
        byte[] encryptedData = encrypt(clearData);
        return Base64New.encodeBytes(encryptedData);
    }

    public byte[] encrypt(byte[] clearData) {
        try {
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        } catch (InvalidKeyException e) {
            Log.e(TAG, "Invalid key", e);
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            Log.e(TAG, "Invalid algorithm " + CIPHER_ALGORITHM, e);
            return null;
        }

        byte[] encryptedData;
        try {
            encryptedData = aesCipher.doFinal(clearData);
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG, "Illegal block size", e);
            return null;
        } catch (BadPaddingException e) {
            Log.e(TAG, "Bad padding", e);
            return null;
        }
        return encryptedData;
    }

    private byte[] encodeDigest(String text) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(MESSAGEDIGEST_ALGORITHM);
            return digest.digest(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such algorithm " + MESSAGEDIGEST_ALGORITHM, e);
        }

        return null;
    }
}
