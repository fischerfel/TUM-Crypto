import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.*;


public class Encryption 
{
    private static final String ALGORITHME = "Blowfish";
    private static final String TRANSFORMATION = "Blowfish/ECB/PKCS5Padding";
    private static final String SECRET = "kjkdfjslm";
    private static final String CHARSET = "ISO-8859-1";


    public static void main(String[] argv) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException
    {
        Encryption main = new Encryption();

        String plain = "trallalla";
        System.out.println("initial : " + plain);

        String encrypted = main.encrypt(plain);
        System.out.println("after encryption : " + encrypted);

        String decrypted = main.decrypt(encrypted);
        System.out.println("after decryption : " + decrypted);
    }

    public String encrypt(String plaintext) 
    throws NoSuchAlgorithmException, 
    NoSuchPaddingException, 
    InvalidKeyException, 
    UnsupportedEncodingException, 
    IllegalBlockSizeException, 
    BadPaddingException
    {

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SECRET.getBytes(CHARSET), ALGORITHME));
            return new String(cipher.doFinal(plaintext.getBytes()), CHARSET);    
    }

    public String decrypt(String ciphertext) 
    throws NoSuchAlgorithmException, 
    NoSuchPaddingException, 
    InvalidKeyException, 
    UnsupportedEncodingException, 
    IllegalBlockSizeException, 
    BadPaddingException 
    {
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SECRET.getBytes(), ALGORITHME));
      return new String(cipher.doFinal(ciphertext.getBytes(CHARSET)), CHARSET);
    }
}
