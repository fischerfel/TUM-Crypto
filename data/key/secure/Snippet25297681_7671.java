import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class aes {
    final static String Plaintext="difficult";
    final static String pswd="secretkey";
    public static Key setkey(String mykey)
    {
        Key skey=null;
        try
        {
             skey=new SecretKeySpec(mykey.getBytes("UTF-8"), "AES");
        }catch(Exception e){
             e.printStackTrace();
        }
        return skey;
    }

    public static String encrypt(String plaintxt)
    {
        String encryptedtxt=null;
        try
        {
            Key skey=aes.setkey(pswd);
            Cipher c=Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, skey);
            byte[] P=plaintxt.getBytes("UTF-8");
            encryptedtxt=new String(c.doFinal(P));
        }catch(Exception e){
            e.printStackTrace();
        }
        return encryptedtxt;
    }

    public static String decrypt(String encryptedtxt)
    {
        try{
            Cipher c=Cipher.getInstance("AES");
            Key skey=aes.setkey(pswd);
            c.init(Cipher.DECRYPT_MODE, skey);
            String decryptedtxt=new String(c.doFinal(encryptedtxt.getBytes("UTF-8")));
            return decryptedtxt;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        System.out.println("Plain text    :"+Plaintext);
        String encryptedtxt=aes.encrypt(Plaintext.trim());
        System.out.println("encrypted text:"+encryptedtxt);
        String decryptedtxt=aes.decrypt(encryptedtxt.trim());
        System.out.println("decrypted text:"+decryptedtxt);
    }
}
