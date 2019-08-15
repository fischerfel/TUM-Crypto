import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.spec.KeySpec;

public final class Encrypter
{
    public static final String DESEDE_ENCRYPTION = "DESede";

    private KeySpec keySpec;
    private SecretKeyFactory keyFactory;
    private Cipher cipher;

    private static final String UNICODE_FORMAT = "UTF8";

    public Encrypter(String encryptionKey)
        throws Exception
    {
        byte[] keyAsBytes = encryptionKey.getBytes(UNICODE_FORMAT);
        keySpec = new DESedeKeySpec(keyAsBytes);
        keyFactory = SecretKeyFactory.getInstance(DESEDE_ENCRYPTION);
        cipher = Cipher.getInstance(DESEDE_ENCRYPTION);
    }

    public String encryptString(String unencryptedString)
    {
        SecretKey key = keyFactory.generateSecret(keySpec);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
        byte[] ciphertext = cipher.doFinal(cleartext);

        BASE64Encoder base64encoder = new BASE64Encoder();
        return base64encoder.encode(ciphertext);
    }

    public String decryptString(String encryptedString)
    {
        SecretKey key = keyFactory.generateSecret(keySpec);
        cipher.init(Cipher.DECRYPT_MODE, key);
        BASE64Decoder base64decoder = new BASE64Decoder();
        byte[] ciphertext = base64decoder.decodeBuffer(encryptedString);
        byte[] cleartext = cipher.doFinal(ciphertext);

        return bytesToString(cleartext);
    }

    private static String bytesToString(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes)
        {
            sb.append((char) aByte);
        }
        return sb.toString();
    }
}
