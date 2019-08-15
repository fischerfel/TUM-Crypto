import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.Validate;

public class Encryptor
{
    private static final String CIPHER_ALOGRITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALOGRITHM = "PBKDF2WithHmacSHA1";
    private static final String KEY_SPEC_ALOGRITHM = "AES";
    private static final String ENCODING = "UTF-8";

    public static String decrypt(String password, String encodedCiphertextAndIv) throws GeneralSecurityException,
        UnsupportedEncodingException
    {
        String[] ra = encodedCiphertextAndIv.split(":");
        byte[] ciphertext = Base64.decodeBase64(ra[0]);
        byte[] iv = Base64.decodeBase64(ra[1]);

        /* Decrypt the message, given derived key and initialization vector. */
        Cipher cipher = Cipher.getInstance(CIPHER_ALOGRITHM);
        cipher.init(Cipher.DECRYPT_MODE, getSecret(password), new IvParameterSpec(iv));
        String plaintext = new String(cipher.doFinal(ciphertext), ENCODING);
        return plaintext;
    }

    static SecretKey getSecret(String password) throws GeneralSecurityException
    {
        byte[] salt = Arrays.copyOf(password.getBytes(), 8);

        /* Derive the key, given password and salt. */
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALOGRITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1024, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), KEY_SPEC_ALOGRITHM);
        return secret;
    }

    public static String encrypt(String password, String plainText) throws GeneralSecurityException,
        UnsupportedEncodingException
    {
        if (plainText == null)
        {
            return plainText;
        }
        String[] plainTextValues = new String[]
        {
            plainText
        };
        String[] encryptedValues = encrypt(password, plainTextValues);
        return encryptedValues[0];
    }

    public static String[] encrypt(String password, String[] plainTextValues) throws GeneralSecurityException,
        UnsupportedEncodingException
    {
        if (plainTextValues == null || plainTextValues.length == 0)
        {
            return new String[] {};
        }        
        Validate.notEmpty(password, "password must not be empty");

        /* Encrypt the message. */
        Cipher cipher = Cipher.getInstance(CIPHER_ALOGRITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getSecret(password));
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        String encodedIv = new String(Base64.encodeBase64(iv));

        String[] encryptedValues = new String[plainTextValues.length];
        for (int i = 0; i < plainTextValues.length; i++)
        {
            String plainText = plainTextValues[i];
            if (plainText == null)
            {
                encryptedValues[i] = plainText;
            }
            else
            {
                byte[] ciphertext = cipher.doFinal(plainText.getBytes(ENCODING));
                String encodedCiphertext = new String(Base64.encodeBase64(ciphertext));
                encryptedValues[i] = encodedCiphertext + ":" + encodedIv;
            }
        }
        return encryptedValues;
    }

    public static void main(String[] args) throws Exception
    {
        String password = "The Secret";
        String plainText = "Hello, World!";

        String enc = encrypt(password, plainText);
        System.out.println(enc);
        System.out.println(decrypt(password, enc));
    }
}
