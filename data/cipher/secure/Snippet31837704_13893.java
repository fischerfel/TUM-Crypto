public class Crypto {

private static final String engine = "AES";
private static final String crypto = "AES/CBC/PKCS5Padding";

static String key = "1234567890987654";
static String _iv = "1234567890987654";

public static byte[] cipher(byte[] data, int mode)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException,
        BadPaddingException, InvalidAlgorithmParameterException 
{
    SecretKeySpec sks = new SecretKeySpec(key.getBytes(), engine);
    IvParameterSpec iv = new IvParameterSpec(_iv.getBytes());
    Cipher c = Cipher.getInstance(crypto);
    c.init(mode, sks, iv);
    return c.doFinal(data);
}

public static byte[] encrypt(byte[] data) throws InvalidKeyException,
        NoSuchAlgorithmException, NoSuchPaddingException,
        IllegalBlockSizeException, BadPaddingException,
        InvalidAlgorithmParameterException {
    return cipher(data, Cipher.ENCRYPT_MODE);
}

public static byte[] decrypt(byte[] data) throws InvalidKeyException,
        NoSuchAlgorithmException, NoSuchPaddingException,
        IllegalBlockSizeException, BadPaddingException,
        InvalidAlgorithmParameterException {
    return cipher(data, Cipher.DECRYPT_MODE);
}
}
