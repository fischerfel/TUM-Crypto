import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;

public class AESplus 
{

// AES specification
private static final String CIPHER_SPEC = "AES/CBC/PKCS5Padding";

// Key derivation specification
private static final String KEYGEN_SPEC = "PBKDF2WithHmacSHA1";
private static final int SALT_LENGTH = 16; // in bytes
private static final int ITERATIONS = 32768;
private static final int KEY_LENGTH = 128;

private static final String SPLITCHAR = "###";

public static SecretKey makeKey(String kennwort) throws NoSuchAlgorithmException, InvalidKeySpecException
{
    char[] password = kennwort.toCharArray();

    // generate salt
    byte[] salt = generateSalt(SALT_LENGTH);

    SecretKeyFactory factory = SecretKeyFactory.getInstance(KEYGEN_SPEC);

    KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
    SecretKey tmp = factory.generateSecret(spec);

    return tmp;
}

private static byte[] generateSalt(int length) 
{
    Random r = new SecureRandom();
    byte[] salt = new byte[length];
    r.nextBytes(salt);
    return salt;
}

public static String encrypt(String input, SecretKey key) throws InvalidKeyLengthException, StrongEncryptionNotAvailableException,
                        IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException,
                        IllegalBlockSizeException, BadPaddingException
{
    StringBuilder output = new StringBuilder(); 

    // initialize AES encryption
    Cipher encrypt = null;
    encrypt = Cipher.getInstance(CIPHER_SPEC);
    encrypt.init(Cipher.ENCRYPT_MODE, key);

    // get initialization vector
    byte[] iv = encrypt.getParameters().getParameterSpec(IvParameterSpec.class).getIV();

    byte[] encrypted = encrypt.update(input.getBytes());
    output.append(HexUtils.toHex(encrypted));

    encrypted = encrypt.doFinal();

    if (encrypted != null)
    {
        // write authentication and AES initialization data
        output.append(HexUtils.toHex(iv) + SPLITCHAR);
        // data
        output.append(HexUtils.toHex(encrypted));
    }

    return output.toString();
}

public static String decrypt(String input, SecretKey schlüssel) throws InvalidPasswordException, InvalidAESStreamException,
                        IOException, StrongEncryptionNotAvailableException, NoSuchAlgorithmException, NoSuchPaddingException,
                        InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException 
{
    String[] inputArray = input.split(SPLITCHAR);

    // initialize AES decryption
    byte[] iv = new byte[16]; // 16-byte I.V. regardless of key size
    iv = HexUtils.toBytes(inputArray[0]);

    Cipher decrypt = Cipher.getInstance(CIPHER_SPEC);
    decrypt.init(Cipher.DECRYPT_MODE, schlüssel, new IvParameterSpec(iv));

    // read data from input into buffer, decrypt and write to output
    byte[] hexInput = HexUtils.toBytes(inputArray[1]);
    byte[] decrypted = decrypt.update(hexInput);

    StringBuilder output = new StringBuilder();
    output.append(new String(decrypted));

    // finish decryption - do final block
    decrypted = decrypt.doFinal();
    if (decrypted != null) 
    {
        output.append(new String(decrypted));
    }

    return output.toString();
}


// ******** EXCEPTIONS thrown by encrypt and decrypt ********

/**
 * Thrown if an attempt is made to decrypt a stream with an incorrect
 * password.
 */
public static class InvalidPasswordException extends Exception 
{
    private static final long serialVersionUID = 1L;
}

/**
 * Thrown if an attempt is made to encrypt a stream with an invalid AES key
 * length.
 */
public static class InvalidKeyLengthException extends Exception 
{
    private static final long serialVersionUID = 1L;

    InvalidKeyLengthException(int length)
    {
        super("Invalid AES key length: " + length);
    }
}

/**
 * Thrown if 192- or 256-bit AES encryption or decryption is attempted,
 * but not available on the particular Java platform.
 */
public static class StrongEncryptionNotAvailableException extends Exception
{
    private static final long serialVersionUID = 1L;

    public StrongEncryptionNotAvailableException(int keySize) 
    {
        super(keySize + "-bit AES encryption is not available on this Java platform.");
    }
}

/**
 * Thrown if an attempt is made to decrypt an invalid AES stream.
 */
public static class InvalidAESStreamException extends Exception
{
    private static final long serialVersionUID = 1L;

    public InvalidAESStreamException() 
    {
        super();
    };

    public InvalidAESStreamException(Exception e)
    {
        super(e);
    }
}
}
