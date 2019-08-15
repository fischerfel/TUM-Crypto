import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class Main 
{
    public static void main(String[] args)
    {
        String pass = "this is the pass";
        char[] pw = new char[pass.length()];
        for(int k=0; k<pass.length();++k)
        {
            pw[k] = pass.charAt(k); 
        }
        try {
            byte[] q = encrypt(pw,"asdf".getBytes(),"der text");
            System.out.println(new String(q));
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static SecretKey getSecretKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        // NOTE: last argument is the key length, and it is 256
        KeySpec spec = new PBEKeySpec(password, salt, 1024, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        return(secret);
    }


    public static byte[] encrypt(char[] password, byte[] salt, String text) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
        SecretKey secret = getSecretKey(password, salt);

        Cipher cipher = Cipher.getInstance("AES");

        // NOTE: This is where the Exception is being thrown
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] ciphertext = cipher.doFinal(text.getBytes("UTF-8"));
        return(ciphertext);
    }
}
