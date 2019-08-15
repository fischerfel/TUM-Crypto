import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author rishad.ali
 *
 */
public class Security
{
    //The secret Key to encrypt and decrypt text
    private SecretKey key = null;

    /**
     * Constructor of the class to generate Key
     */
    public Security(String secretCode)
    {
        try
        {
            DESKeySpec keySpec = new DESKeySpec(secretCode.getBytes("UTF8")); 
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(keySpec);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } 
    }
    /**
     * Returns the encrypted text for the given plain text.
     * 
     * @param plainText - given plain text
     * @return
     */
    public String encrypt (String plainText)
    {
        BASE64Encoder base64encoder = new BASE64Encoder();
        //Encrypted Text to be sent
        String encryptedText = "";
        //Encrypt the plain text
        try
        {
            byte[] cleartext = plainText.getBytes("UTF8");      
            Cipher cipher = Cipher.getInstance("DES"); 
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encryptedText = base64encoder.encode(cipher.doFinal(cleartext));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } 
        return encryptedText;
    }

    /**
     * Returns the plain text for the given encrypted Text
     * 
     * @param encryptedText - Given Encrypted Text
     * @return
     */
    public String decrypt (String encryptedText)
    {
        sun.misc.BASE64Decoder base64decoder = new BASE64Decoder();
        //Plain text to be sent
        String plainText = "";
        // Decrypt the  encrypted text
        try
        {
            byte[] encrypedPwdBytes = base64decoder.decodeBuffer(encryptedText);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));
            plainText = new String(plainTextPwdBytes, "US-ASCII");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } 
        return plainText;
    }

    public static void main (String args [])
    {
        Security sec = new Security("secrtkey");
        String text = "sometext";
        System.out.println("Encrypted Text : "+sec.encrypt(text));
    }
}
