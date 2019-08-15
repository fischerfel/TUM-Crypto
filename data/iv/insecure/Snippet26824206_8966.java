import java.security.AlgorithmParameters; 
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
public class SymmEncryption {
public static void main(String[] args) throws InvalidAlgorithmParameterException {
    try {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // 56 is the keysize. Fixed for DES
        //Initialization Vector
        byte[] iv = "1234567812345678".getBytes();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKey secretKey = keyGenerator.generateKey();
        System.out.println(secretKey.getFormat());
        Cipher desCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//algorithm/mode/padding
        //Electronic Codebook (ECB)
        System.out.format("Secret Key: %s--%s--%s%n", secretKey.getAlgorithm(), secretKey.getFormat(), secretKey.getEncoded());
        // Initialize the cipher for encryption
        desCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        // sensitive information
        byte[] text = "No body can see me".getBytes();
        System.out.println("Hex text: " + byteArrayToHex(text));
        System.out.println("Text [Byte Format] : " + text);
        System.out.println("Text : " + new String(text));
        // Encrypt the text
        byte[] textEncrypted = desCipher.doFinal(text);
        System.out.println("Text Encryted : " + textEncrypted);
        System.out.println("Hex Encrypted text: " + byteArrayToHex(textEncrypted));
        // Initialize the same cipher for decryption
        desCipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        // Decrypt the text
        byte[] textDecrypted = desCipher.doFinal(textEncrypted);
        System.out.println("Text Decryted : " + new String(textDecrypted));
        System.out.println("Hex Decrypted text: " + byteArrayToHex(textDecrypted));
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    }
}
static String byteArrayToHex(byte[] a) {
    StringBuilder sb = new StringBuilder();
    for (byte b : a)
        sb.append(String.format("%02x", b & 0xff));
    return sb.toString();
}
}
