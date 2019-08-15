import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES 
{
    SecretKey key;
    DES(byte []  KEY) throws Exception
    {
        key = readKey(KEY);
    }

    public SecretKey readKey(byte[] rawkey) throws Exception
    {
         DESKeySpec keyspec = new DESKeySpec(rawkey);
         SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DES");
         SecretKey key = keyfactory.generateSecret(keyspec);
         return key;
    }

    public byte[] encrypt(byte[] plain ) throws Exception
    {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plain);
        return encrypted;
    }
    public byte[] decrypt(byte[] cipher ) throws Exception
    {
        Cipher dcipher = Cipher.getInstance("DES");
        dcipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = dcipher.doFinal(cipher);
        return decrypted;
    }  
}
