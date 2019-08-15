import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
static String IV = "AAAAAAAAAAAAAAAA";
static String encryptionKey = "0123456789abcdef";

public static byte[] encrypt(byte[] inputcum) throws Exception {
Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
return cipher.doFinal(inputcum);
}

public static byte[] decrypt(byte[] cipherSound) throws Exception{
Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
return cipher.doFinal(cipherSound);
}
}
