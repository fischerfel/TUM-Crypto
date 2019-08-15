package encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Receiver {
    private Cipher decoder;
    private SecretKeySpec myKey;
    public Receiver()throws Exception{

        decoder=Cipher.getInstance("AES");
    }
    public void setKey(byte[] key) throws Exception{

        myKey= new SecretKeySpec(key ,"AES");
        decoder.init(Cipher.DECRYPT_MODE, myKey);
    }
    public byte[] receive(byte[] message) throws Exception{
        return decoder.doFinal(message);
    }
}
