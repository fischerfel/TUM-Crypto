import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.*;

import org.apache.commons.codec.binary.Base64;

public class FrontierCipher
{
    private static final String ALGO = "AES";
    private static String keyString = "00112233445566778899AABBCCDDEEFF0123456789ABCDEF0123456789ABCDEF";

     private static Key generateKey() throws Exception 
     {
        Key key = new SecretKeySpec(convertToByteArray(keyString), ALGO);
        return key;
    }
    public static byte[] encryptBytes(byte[] data) throws Exception
    {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);

        byte[] encVal = c.doFinal(data);
        byte[] encryptedValue = Base64.encodeBase64(encVal);

        return encryptedValue;
    }

    public static byte[] decrpytBytes(byte[] encryptedData) throws Exception
    {   
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);

        byte[] decordedValue = Base64.decodeBase64(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);

        return decValue;
    }

    public static byte[] convertToByteArray(String key) throws KeySizeException
    {
        if(key.length()<64)
            throw new KeySizeException("Key must contain 64 characters");

        byte[] b = new byte[32];

        for(int i=0, bStepper=0; i<key.length()+2; i+=2)
            if(i !=0)
                b[bStepper++]=((byte) Integer.parseInt((key.charAt(i-2)+""+key.charAt(i-1)), 16));

        return b;
    }


    public static void main(String[] args) throws Exception
    {
        byte[] password  = {6,75,3};
        byte[] passwordEnc = encryptBytes(password);
        byte[] passwordDec = decrpytBytes(passwordEnc);

        System.out.println("Plain Text : " + password[0]+" "+ password[1]+" "+ password[2]);
        System.out.println("Encrypted Text : " + passwordEnc[0]+" "+ passwordEnc[1]+" "+ passwordEnc[2]);
        System.out.println("Decrypted Text : " + passwordDec[0]+" "+passwordDec[1]+" "+passwordDec[2]);
    }
}
