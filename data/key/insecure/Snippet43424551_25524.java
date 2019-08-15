package encrypt;

import java.security.Key;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PasswordEncrypt {

String key = "passwordencrypts";
Cipher cipher;
Key aesKey;

public PasswordEncrypt() {
    try {
        aesKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher = Cipher.getInstance("AES");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static void main(String[] args) {
    PasswordEncrypt app = new PasswordEncrypt();
    System.out.println("enter string");
    PasswordEncrypt encrypt = new PasswordEncrypt();
    byte[] en = encrypt.encrypt(new Scanner(System.in).next());

    System.out.println(en + " encrypted");
    String de = encrypt.decrypt(en);
    System.out.println(de + "   decrypted");

}

private String decrypt(byte[] en) {
    String decrypted = "";
    try {
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        decrypted = new String(cipher.doFinal(en));
    } catch (Exception e) {
        e.printStackTrace();
    }
    return decrypted;
}

private byte[] encrypt(String text) {
    byte[] en = null;
    try {
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        en = cipher.doFinal(text.getBytes());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return en;
}
}
