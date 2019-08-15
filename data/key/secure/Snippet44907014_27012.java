import java.io.File;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static final String key = "wesurexZ5!Hcurfit";
    private static final String ivs = "zK2hzBvP%FRJ5%lD";
    public static byte[] encrypt(byte[] strInBytes) throws Exception {
        SecretKeySpec skeySpec = getKey(key);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivs.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        return cipher.doFinal(strInBytes);
    }

    public static byte[] decrypt(byte[] strIn) throws Exception {
        SecretKeySpec skeySpec = getKey(key);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivs.getBytes("UTF-8"));
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] decrypted = cipher.doFinal(strIn);   
        return decrypted;
    }

    private static SecretKeySpec getKey(String strKey) throws Exception {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[16];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");
        return skeySpec;
    }
}
