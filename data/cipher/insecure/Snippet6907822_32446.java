import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class TripleDES 
{
    SecretKey key;

    TripleDES(byte [] rawkey) throws Exception
    {
        key = readKey(rawkey);
    }

    public SecretKey readKey(byte[] rawkey) throws Exception
    {
         DESedeKeySpec keyspec = new DESedeKeySpec(rawkey);
         SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
         SecretKey key = keyfactory.generateSecret(keyspec);
         return key;
    }

    public byte[] encrypt(byte[] plain ) throws Exception
    {
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plain);
        return encrypted;
    }
    public byte[] decrypt(byte[] cipher ) throws Exception
    {
        Cipher dcipher = Cipher.getInstance("DESede");
        dcipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = dcipher.doFinal(cipher);
        return decrypted;
    }  
}
