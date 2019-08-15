import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MCrypt {

    private int iterationCount = 10000;
    private int saltLength = 8; // bytes; 64 bits
    private int keyLength = 128;

    public static void main(String[] args) throws Exception {
        MCrypt mc = new MCrypt();
        String encryptedData = mc.encrypt("1234");
        MCrypt mc1 = new MCrypt();
        System.out.println(new String(mc1.decrypt(new String(encryptedData),
                "1234"), "UTF-8"));
    }

    public MCrypt() {
    }

    public String encrypt(String text) throws Exception {
        if (text == null || text.length() == 0)
            throw new Exception("Empty string");

        byte[] encrypted = null;

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[saltLength];
        random.nextBytes(salt);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = new byte[cipher.getBlockSize()];
        random.nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        KeySpec keySpec = new PBEKeySpec(text.toCharArray(), salt,
                iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);

        encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        String encryptedStr = Base64.encodeBytes(encrypted);

        StringBuffer strBuf = new StringBuffer();
        strBuf.append(new String(encryptedStr));
        strBuf.append("]");
        strBuf.append(new String(salt));
        strBuf.append("]");
        strBuf.append(new String(iv));

        return new String(Base64.encodeBytes(strBuf.toString().getBytes()));
    }

    public byte[] decrypt(String code, String pwd) throws Exception {
        if (code == null || code.length() == 0)
            throw new Exception("Empty string");

        String[] fields = new String(Base64.decode(code)).split("]");
        byte[] cipherBytes = Base64.decode(fields[0]);
        byte[] salt = fields[1].getBytes();
        byte[] iv = fields[2].getBytes();

        KeySpec keySpec = new PBEKeySpec(pwd.toCharArray(), salt,
                iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        byte[] decrypted = null;
        cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
        decrypted = cipher.doFinal(cipherBytes);

        return decrypted;
    }

}
