package cipher;

import java.io.*;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class Ciphera
{
    private SecretKey key;
    private Cipher enc;
    private Cipher dec;
    public void initialize()
    {
        try
        {
            File keyf = new File("key.key");
            FileInputStream fread = new FileInputStream(keyf);
            byte[] enckey = new byte[(int)keyf.length()];
            fread.read(enckey);
            fread.close();
            key = new SecretKeySpec(enckey, "AES");


            enc = Cipher.getInstance("AES");
            enc.init(Cipher.ENCRYPT_MODE, key);
            dec = Cipher.getInstance("AES");
            dec.init(Cipher.DECRYPT_MODE, key);
        }
        catch (Exception e)
        {
        }
    }

    //Encoder
    public String encode(String str)
    {
        String decr = "";
        try
        {
            byte[] encr = enc.doFinal(str.getBytes());
            decr = Base64.getEncoder().encodeToString(encr);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return decr;
    }

    //Decoder
    public String decode(String str)
    {
        String decr = "";
        try
        {
            byte[] temp = Base64.getDecoder().decode(str);
            temp = dec.doFinal(temp);
            for(int i = 0; i < temp.length; i++)
            {
                decr += (char)temp[i];
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return decr;
    }
}
