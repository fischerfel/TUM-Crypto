import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor2 {
    private IvParameterSpec ivSpec;
    private SecretKeySpec keySpec;
    private Cipher cipher;

    public static void main(String[] args) throws Exception {
        Encryptor2 e = new Encryptor2(
                "averylongtext!@$@#$#@$#*&(*&}{23432432432dsfsdf");
        String enc = e.encrypt("john doe");
        String dec = e.decrypt(enc);
    }

    public Encryptor2(String pass) throws Exception {
        // setup AES cipher in CBC mode with PKCS #5 padding
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // setup an IV (initialization vector) that should be
        // randomly generated for each input that's encrypted
        byte[] iv = new byte[cipher.getBlockSize()];
        new SecureRandom().nextBytes(iv);
        ivSpec = new IvParameterSpec(iv);

        // hash keyString with SHA-256 and crop the output to 128-bit for key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(pass.getBytes());
        byte[] key = new byte[16];
        System.arraycopy(digest.digest(), 0, key, 0, key.length);
        keySpec = new SecretKeySpec(key, "AES");
    }

    public String encrypt(String original) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(original.getBytes("UTF-8"));
        System.out.println("encrypted: `" + new String(encrypted) + "`");
        return new String(encrypted);
    }

    public String decrypt(String encrypted) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decrypted = cipher.doFinal(encrypted.getBytes("UTF-8"));
        System.out.println("decrypted: `" + new String(decrypted, "UTF-8")
                + "`");
        return new String(decrypted, "UTF-8");
    }
}
