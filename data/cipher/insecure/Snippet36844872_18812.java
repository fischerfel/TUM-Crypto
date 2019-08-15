import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

private static String encryptString(String value, String key)
{
    String encryptedString = "";
    if (value != null)
    {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
        try
        {
            Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS#5");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            encryptedString = new String(Hex.encodeHex(encrypted));
        }
        catch (Exception e)
        {
            // Show error
        }
    }
    return encryptedString;
}
