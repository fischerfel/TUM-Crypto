public class ObjectCrypter {


public static byte[] encrypt(byte[] ivBytes, byte[] keyBytes, byte[] mes) 
        throws NoSuchAlgorithmException,
        NoSuchPaddingException,
        InvalidKeyException,
        InvalidAlgorithmParameterException,
        IllegalBlockSizeException,
        BadPaddingException, IOException {

    AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = null;
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
    return  cipher.doFinal(mes);

}

public static byte[] decrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes) 
        throws NoSuchAlgorithmException,
        NoSuchPaddingException,
        InvalidKeyException,
        InvalidAlgorithmParameterException,
        IllegalBlockSizeException,
        BadPaddingException, IOException, ClassNotFoundException {

    AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
    return  cipher.doFinal(bytes);

}
