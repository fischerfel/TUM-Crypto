package cartoon;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MCrypt {

private String iv = "0123456789abcdef";// iv 
private IvParameterSpec ivspec;
private SecretKeySpec keyspec;
private Cipher cipher;
private String SecretKey = "fedcba9876543210";// secretKey 

public MCrypt() {
    ivspec = new IvParameterSpec(iv.getBytes(), 0, iv.getBytes().length);

    keyspec = new SecretKeySpec(SecretKey.getBytes(), 0, iv.getBytes().length, "AES");

}

String Decrypt(String text) throws Exception {
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
    byte[] results = null;
    int results1 = cipher.doFinal(Base64.decode(text), 0, Base64.decode(text).length, results, 0);
    System.out.println("String resultssssssssssssss " + results1);
    return new String(results, "UTF-8");
}

String Encrypt(String text)
        throws Exception {
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    System.out.println("String input : " + text);

    cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
    byte[] results = null;
    int results1 = cipher.doFinal(text.getBytes(), 0, text.getBytes().length, results, 0);
    return Base64.encode(results);
}
}
