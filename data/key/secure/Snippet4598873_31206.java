import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public final class StupidSimpleEncrypter
{
    public static String encrypt(String key, String plaintext)
    {
        byte[] keyBytes = key.getBytes();
        byte[] plaintextBytes = plaintext.getBytes();
        byte[] ciphertextBytes = encrypt(keyBytes, plaintextBytes);
        return new String(ciphertextBytes);
    }

    public static byte[] encrypt(byte[] key, byte[] plaintext)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(getRawKey(key), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, spec);
            return cipher.doFinal(plaintext);
        }
        catch(Exception e)
        {
            // some sort of problem, return null because we can't encrypt it.
            Utility.writeError(e);
            return null;
        }
    }

    public static String decrypt(String key, String ciphertext)
    {
        byte[] keyBytes = key.getBytes();
        byte[] ciphertextBytes = ciphertext.getBytes();
        byte[] plaintextBytes = decrypt(keyBytes, ciphertextBytes);
        return new String(plaintextBytes);
    }

    public static byte[] decrypt(byte[] key, byte[] ciphertext)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(getRawKey(key), "AES");
            cipher.init(Cipher.DECRYPT_MODE, spec);
            return cipher.doFinal(ciphertext);
        }
        catch(Exception e)
        {
            // some sort of problem, return null because we can't encrypt it.
            Utility.writeError(e);
            return null;
        }
    }

    private static byte[] getRawKey(byte[] key)
    {
        try
        {
            KeyGenerator gen = KeyGenerator.getInstance("AES");
            SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
            rand.setSeed(key);
            gen.init(256, rand);
            return gen.generateKey().getEncoded();
        }
        catch(Exception e)
        {
            return null;
        }
    }
}
