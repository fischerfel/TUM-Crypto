package des;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;

public final class Des
{
    private static final byte[] IV = {(byte) 0xA6, (byte) 0x8A, 0x11, 0x63, (byte) 0x94, 0x4D, (byte) 0x8E, (byte) 0xA3};
    private static final byte[] DES_KEY = {(byte) 0x81, 0x33, 0x66, (byte) 0xD8, 0x5F, (byte) 0xD3, 0x17, 0x21, 0x5C, 0x7F};

    public static byte [] encrypt(String data)
    {
        byte result[] = null;
        try
        {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(DES_KEY);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(IV);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            result = cipher.doFinal(data.getBytes(CHARSET));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }  
}
