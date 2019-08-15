import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javax.xml.bind.DatatypeConverter;

public class Encryptor {
    private static String algorithm = "RC4";
    public static String encrypt(String key, String value) {
        try {
            SecretKeySpec rc4Key = new SecretKeySpec(key.getBytes(), algorithm);
            Cipher rc4 = Cipher.getInstance(algorithm);

            rc4.init(Cipher.ENCRYPT_MODE, rc4Key);
            byte [] encrypted = rc4.update(value.getBytes());
            return DatatypeConverter.printBase64Binary(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String key = "trololol";
        String value = "0612345678";

        System.out.println(encrypt(key, value));
    }
}
