import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AESplus 
{
public static SecretKeySpec makeKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException
{
password = String.format("%040x", new BigInteger(1,password.getBytes(Charset.forName("UTF-8"))));
    password = password.substring(password.length()-32, password.length());
    final byte[] symKeyData = DatatypeConverter.parseHexBinary(password);
    return new SecretKeySpec(symKeyData, "AES");
}


public static String encryptString(String text, SecretKeySpec key) throws NoSuchAlgorithmException, NoSuchPaddingException, 
                    InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
{
    final byte[] encodedMessage = text.getBytes(Charset.forName("UTF-8"));

    final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    final int blockSize = cipher.getBlockSize();

    // generate random IV using block size
    final byte[] ivData = new byte[blockSize];
    final SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
    rnd.nextBytes(ivData);
    final IvParameterSpec iv = new IvParameterSpec(ivData);

    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
    final byte[] encryptedMessage = cipher.doFinal(encodedMessage);

    // concatenate IV and encrypted message
    final byte[] ivAndEncryptedMessage = new byte[ivData.length + encryptedMessage.length];
    System.arraycopy(ivData, 0, ivAndEncryptedMessage, 0, blockSize);
    System.arraycopy(encryptedMessage, 0, ivAndEncryptedMessage, blockSize, encryptedMessage.length);
    return DatatypeConverter.printBase64Binary(ivAndEncryptedMessage);
}


public static String decrytString(String crypttext, SecretKeySpec key) throws NoSuchAlgorithmException, NoSuchPaddingException, 
                        InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException 
{    
    final byte[] ivAndEncryptedMessage = DatatypeConverter.parseBase64Binary(crypttext);

    final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    final int blockSize = cipher.getBlockSize();

    // retrieve random IV from start of the received message
    final byte[] ivData = new byte[blockSize];
    System.arraycopy(ivAndEncryptedMessage, 0, ivData, 0, blockSize);
    final IvParameterSpec iv = new IvParameterSpec(ivData);

    // retrieve the encrypted message itself
    final byte[] encryptedMessage = new byte[ivAndEncryptedMessage.length - blockSize];
    System.arraycopy(ivAndEncryptedMessage, blockSize, encryptedMessage, 0, encryptedMessage.length);

    cipher.init(Cipher.DECRYPT_MODE, key, iv);
    final byte[] encodedMessage = cipher.doFinal(encryptedMessage);

    // concatenate IV and encrypted message
    final String message = new String(encodedMessage,Charset.forName("UTF-8"));

    return message;
}
