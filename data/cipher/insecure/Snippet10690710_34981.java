public class AESEncryption {

private SecretKeySpec keySpec;

public AESEncryption()
{
    super();
    setKeySpec(AES256Encryption.generateAES256KeySpec());
}

// Uses 256-bit encryption by default.
public static SecretKeySpec generateAES256KeySpec()
{
    // Stack variables
    byte[] byteArray = new byte[16];
    SecretKey oTmpKey = null;
    KeyGenerator oKeyGen;
    try
    {
        oKeyGen = KeyGenerator.getInstance("AES");
        oKeyGen.init(256);
        oTmpKey = oKeyGen.generateKey();
    }
    catch(Throwable oThrown)
    {
        throw new RuntimeException(oThrown);
    }

    byteArray = oTmpKey.getEncoded();

    return new SecretKeySpec(byteArray, "AES");
}

public String encrypt(final String p_strPlaintext)
{
    String strEncrypted = null;

    try
    {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        strEncrypted = Base64.encodeBase64String(cipher
            .doFinal(p_strPlaintext.getBytes()));
    }
    catch(Throwable oThrown)
    {
        System.out.println(oThrown.getMessage());
        throw new RuntimeException(oThrown);
    }

    return strEncrypted;
}

}
