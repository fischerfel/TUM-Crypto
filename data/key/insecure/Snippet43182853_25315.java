public class AES256Cipher {
static byte[] ivBytes = new byte[]{0x49, 0x76, 0x61, 0x6e, 0x20, 0x4d, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76};
static String EncryptionKey = "abc123";

public static byte[] encrypt(String plainText)
        throws java.io.UnsupportedEncodingException,
        NoSuchAlgorithmException,
        NoSuchPaddingException,
        InvalidKeyException,
        InvalidAlgorithmParameterException,
        IllegalBlockSizeException,
        BadPaddingException {
    byte[] keyBytes = EncryptionKey.getBytes("UTF-8");

    AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = null;
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
    byte[] cipherData = cipher.doFinal(plainText.getBytes("UTF-8"));
    Log.e("cipher", Base64.encodeToString(cipherData, Base64.DEFAULT));
    return cipher.doFinal(plainText.getBytes("UTF-8"));
}
}
