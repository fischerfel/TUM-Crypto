import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

// Make a blank 256 Bit AES Key
final SecretKey secretKey = new SecretKeySpec(new byte[32], "AES");
final Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
// This line will throw a invalid key length exception if you don't have
// JCE Unlimited strength installed
encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
// If it makes it here, you have JCE installed
