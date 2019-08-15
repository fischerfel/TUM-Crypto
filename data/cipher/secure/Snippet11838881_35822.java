import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class TestDecrpt {
    public static void main(String[] args) throws Exception {
        String data = "encrypted data";
        String sEncryptionKey = "encryption key";
        byte[] rawData = new Base64().decode(data);
        byte[] salt = new byte[8];
        System.arraycopy(rawData, 0, salt, 0, salt.length);

        Rfc2898DeriveBytes keyGen = new Rfc2898DeriveBytes(sEncryptionKey, salt);

        byte[] IV = keyGen.getBytes(128 / 8);
        byte[] keyByte = keyGen.getBytes(256 / 8);

        Key key = new SecretKeySpec(keyByte, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));
        int pureDataLength = rawData.length - 8;
        byte[] pureData = new byte[pureDataLength];
        System.arraycopy(rawData, 8, pureData, 0, pureDataLength);
        String plaintext = new String(cipher.doFinal(pureData), "UTF-8").replaceAll("\u0000", "");
        System.out.println(plaintext);
    }
}
