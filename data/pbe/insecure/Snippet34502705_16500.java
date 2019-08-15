package test;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Test {

    public static final String PASSWORD = "97DE:4F76";

    public static String encryptString(String clearText, String password) {
        return "";
    }

    // echo U2FsdGVkX18PnO/NLSxJ1pg6OKoLyZApMz7aBRfKhJc= | openssl enc -d -a -aes-256-cbc -md sha256 -pass pass:97DE:4F76
    //
    // see https://stackoverflow.com/a/992413, https://stackoverflow.com/a/15595200,
    // https://stackoverflow.com/a/22445878, https://stackoverflow.com/a/11786924
    public static String decryptString(String cypherText, String password) {
        byte[] dataBase64 = DatatypeConverter.parseBase64Binary(cypherText);
        byte[] salt = {
                (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
                (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0
        };

        try {
            // generate the key
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); // "PBKDF2WithHmacSHA1"
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            // decrypt the message
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));

            byte[] decrypted = cipher.doFinal(dataBase64);
            String answer = new String(decrypted, "UTF-8");
            return answer;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(decryptString("U2FsdGVkX18PnO/NLSxJ1pg6OKoLyZApMz7aBRfKhJc=", PASSWORD));
    }
}
