import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

public class AESCrypt{

public static final byte[] encBytes(byte[] srcBytes, byte[] key,
        byte[] newIv) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    IvParameterSpec iv = new IvParameterSpec(newIv);
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
    byte[] encrypted = cipher.doFinal(srcBytes);
    return encrypted;
}

public static final String encText(String sSrc)
        throws Exception {
    byte[] key = 
    {'a','r','e','y','o','u','o','k','a','r','e','y','o','u','o','k'};
    byte[] ivk = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    byte[] srcBytes = sSrc.getBytes("utf-8");
    byte[] encrypted = encBytes(srcBytes, key, ivk);
    return Base64.encodeToString(encrypted,Base64.DEFAULT);
}

}
