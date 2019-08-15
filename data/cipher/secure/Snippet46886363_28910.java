import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RC6 {

    static String IV = "AAAAAAAAAAAAAAAA";
    static String initkey = "qwertyuiopasdfgh"; // 16 characters

    public static void main(String[] args) throws Exception {

        byte toEncrypt[] = { 23, 68, 45, 65, 21, 34, 22, 90 };
        Security.addProvider(new BouncyCastleProvider());
        System.out.println(Arrays.toString(toEncrypt));
        System.out.println("Encrypting...");

        byte[] encrypted = encrypt(toEncrypt, 0, toEncrypt.length);
        System.out.println(Arrays.toString(encrypted));
        System.out.println("Decrypting...");
        byte[] decrypted = decrypt(encrypted, 0, toEncrypt.length);

        System.out.println(Arrays.toString(decrypted));
    }

    public static byte[] encrypt(byte[] plainData, int offset, int lenght) 
    throws Exception {

        Cipher cipher = Cipher.getInstance("RC6/CTR/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(initkey.getBytes("UTF-8"), 
        "RC6");
        cipher.init(Cipher.ENCRYPT_MODE, key, new 
        IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(plainData, offset, lenght);

    }

    public static byte[] decrypt(byte[] cipherSound, int offset, int lenght) 
    throws Exception {

        Cipher cipher = Cipher.getInstance("RC6/CTR/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(initkey.getBytes("UTF-8"), 
        "RC6");
        cipher.init(Cipher.DECRYPT_MODE, key, new 
        IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(cipherSound);
}
}
