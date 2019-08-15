import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES 
{
    public static byte[] encrypt(final byte[] keyBytes, final byte[] ivBytes, final byte[] messageBytes) throws InvalidKeyException, InvalidAlgorithmParameterException
    {       
        return AES.transform(Cipher.ENCRYPT_MODE, keyBytes, ivBytes, messageBytes);
    }

    public static byte[] decrypt(final byte[] keyBytes, final byte[] ivBytes, final byte[] messageBytes) throws InvalidKeyException, InvalidAlgorithmParameterException
    {       
        return AES.transform(Cipher.DECRYPT_MODE, keyBytes, ivBytes, messageBytes);
    }

    private static byte[] transform(final int mode, final byte[] keyBytes, final byte[] ivBytes, final byte[] messageBytes) throws InvalidKeyException, InvalidAlgorithmParameterException
    {
        final SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        final IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        byte[] transformedBytes = null;

        try
        {
            final Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

            cipher.init(mode, keySpec, ivSpec);

            transformedBytes = cipher.doFinal(messageBytes);
        }        
        catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) 
        {
            e.printStackTrace();
        }
        return transformedBytes;
    }

    public static void main(final String[] args) throws InvalidKeyException, InvalidAlgorithmParameterException
    {
        //Retrieved from a protected local file.
        //Do not hard-code and do not version control.
        final String base64Key = "ABEiM0RVZneImaq7zN3u/w==";

        //Retrieved from a protected database.
        //Do not hard-code and do not version control.
        final String shadowEntry = "AAECAwQFBgcICQoLDA0ODw==:ZtrkahwcMzTu7e/WuJ3AZmF09DE=";

        //Extract the iv and the ciphertext from the shadow entry.
        final String[] shadowData = shadowEntry.split(":");        
        final String base64Iv = shadowData[0];
        final String base64Ciphertext = shadowData[1];

        //Convert to raw bytes.
        final byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        final byte[] ivBytes = Base64.getDecoder().decode(base64Iv);
        final byte[] encryptedBytes = Base64.getDecoder().decode(base64Ciphertext);

        //Decrypt data and do something with it.
        final byte[] decryptedBytes = AES.decrypt(keyBytes, ivBytes, encryptedBytes);

        //Use non-blocking SecureRandom implementation for the new IV.
        final SecureRandom secureRandom = new SecureRandom();

        //Generate a new IV.
        secureRandom.nextBytes(ivBytes);

        //At this point instead of printing to the screen, 
        //one should replace the old shadow entry with the new one.
        System.out.println("Old Shadow Entry      = " + shadowEntry);
        System.out.println("Decrytped Shadow Data = " + new String(decryptedBytes, StandardCharsets.UTF_8));
        System.out.println("New Shadow Entry      = " + Base64.getEncoder().encodeToString(ivBytes) + ":" + Base64.getEncoder().encodeToString(AES.encrypt(keyBytes, ivBytes, decryptedBytes)));
    }
}
