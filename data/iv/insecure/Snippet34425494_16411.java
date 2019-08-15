public class MyDES {
private static String ENCRYPTION_KEY_TYPE = "DESede";
private static String ENCRYPTION_ALGORITHM = "DESede/CBC/NoPadding";

private static byte[] key = new byte[]{
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

private static byte[] iv = new byte[]{
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

public static byte[] encrypt(byte[] plainText) {
    try {
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKey secretKey = new SecretKeySpec(key, ENCRYPTION_KEY_TYPE);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted = cipher.doFinal(plainText);
        return encrypted;
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    }
    return null;
}

public static byte[] decrypt(byte[] cipherText) {
    try {
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKey secretKey = new SecretKeySpec(key, ENCRYPTION_KEY_TYPE);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted = cipher.doFinal(cipherText);
        return decrypted;
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    }
    return null;
}
