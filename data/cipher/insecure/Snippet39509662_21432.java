import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.prefs.Preferences;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

public class Test {
    private Test() {  } 
    /**
     * gets the AES encryption key. 
     * @return
     * @throws Exception
     */
    public static SecretKey getSecretEncryptionKey() throws Exception 
    {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); 
        SecretKey secKey = generator.generateKey();
        return secKey;
    } 
    /**
     * Encrypts password in AES using the secret key.
     * @param passWord
     * @param secKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptText(String passWord,SecretKey secKey) throws Exception 
    {        
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(passWord.getBytes());
        return byteCipherText;
    }
    /**
     * Decrypts encrypted byte array using the key used for encryption.
     * @param byteCipherText
     * @param secKey
     * @return
     * @throws Exception
     */
    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception 
    {
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }
    //converting byte[] to string
    private static String bytesToString(byte[] bytesArray)
    {         
        StringBuffer stringBuffer = new StringBuffer();         
        for (int i = 0; i < bytesArray.length; i++) {             
            stringBuffer.append((char) bytesArray[i]);         
        }         
        return stringBuffer.toString();     
    }

    public static void main(String args[]) throws Exception 
    {
        SecretKey secKey = getSecretEncryptionKey();        
        String s = null;        
        String Username = null;
        String Password = null;     
        String value = null;    
        try 
        {
            if(args[0] != null)
                Username = args[0];
            if(args[1] != null)
                Password = args[1];     
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException caught");
        }
        finally {           
        } 
        byte[] cipherText = encryptText(Password, secKey);
        s = bytesToString(cipherText);      //junk value getting here, i'm expecting same encrypted value here even after converting byte[] to string
        System.out.println("Encrypted cipherText = " + cipherText);
        System.out.println("Encrypted Password = " + s);        
        System.out.println("Done." );
    }
}
