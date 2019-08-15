import android.content.Context;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor
{
Context context;
private static final String WORD = "aefbjolpigrschnx";
private static final String KEY = "kumyntbrvecwxasqertyplmqazwsxedc";
private final static String HEX = "0123456789ABCDEF";


public Encryptor(Context c)
{
    context = c;
}

public String getEncryptedPasswd()
{
    String ivHex = "";
    String encryptedHex = "";

    try 
    {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        ivHex = toHex(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");


        Cipher encryptionCipher = Cipher.getInstance("AES/CBC");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
        byte[] encryptedText = encryptionCipher.doFinal(WORD.getBytes("UTF-8"));
        encryptedHex = toHex(encryptedText);
    } 
    catch (Exception e) 
    {
    }
    return ivHex + encryptedHex;
}

public static String toHex(byte[] buf) 
{
    if (buf == null)
    {
        return "";
    }       
    StringBuffer result = new StringBuffer(2 * buf.length);
    for (int i = 0; i < buf.length; i++)
    {
            result.append(HEX.charAt((buf[i]>>4)&0x0f)).append(HEX.charAt(buf[i]&0x0f));

    }
    return result.toString();

}
