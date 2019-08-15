import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherTest
{

    private static class EncryptInfo
    {

        private final byte[] encryptedData;
        private final byte[] initVector;
        private final byte[] salt;

        public EncryptInfo(byte[] encryptedData, byte[] initVector, byte[] salt)
        {
            this.encryptedData = encryptedData.clone();
            this.initVector = initVector.clone();
            this.salt = salt.clone();
        }

        public byte[] getEncryptedData()
        {
            return encryptedData;
        }

        public byte[] getInitVector()
        {
            return initVector;
        }

        public byte[] getSalt()
        {
            return salt;
        }

    }

    private static final String keyGenAlgorithm = "PBEWithMD5AndDES";
    private static final String keyAlgorithm = "DES";
    private static final String cipherTransform = "PBEWithMD5AndDES/CBC/PKCS5Padding";

    private static EncryptInfo encrypt(char[] password, byte[] data)
            throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException,
            InvalidParameterSpecException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException
    {

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        PBEKeySpec keySpec = new PBEKeySpec(password, salt, 1024);

        SecretKeyFactory secretKeyFactory = SecretKeyFactory
                .getInstance(keyGenAlgorithm);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        keySpec.clearPassword();
        byte[] key = secretKey.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, keyAlgorithm);
        Cipher cipher = Cipher.getInstance(cipherTransform);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] initVector = cipher.getParameters().getParameterSpec(
                IvParameterSpec.class).getIV();

        return new EncryptInfo(cipher.doFinal(data), initVector, salt);
    }

    public static byte[] decrypt(byte[] data, char[] password, byte[] salt,
            byte[] initVector) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException
    {
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, 1024);

        SecretKeyFactory secretKeyFactory = SecretKeyFactory
                .getInstance(keyGenAlgorithm);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        keySpec.clearPassword();
        byte[] key = secretKey.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, keyAlgorithm);
        Cipher cipher = Cipher.getInstance(cipherTransform);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(
                initVector));
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception
    {
        char[] password = "password".toCharArray();

        EncryptInfo info = encrypt(password, "Message".getBytes());

        byte[] decyptedText = decrypt(info.getEncryptedData(), password, info
                .getSalt(), info.getInitVector());

        System.out.println(new String(decyptedText));

    }
}
