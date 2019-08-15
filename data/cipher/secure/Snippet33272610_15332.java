public class EncryptDecrypt {

private static final String SECRET_KEY_1 = "ssdkF$HUy2A#D%kd";
private static final String SECRET_KEY_2 = "weJiSEvR5yAC5ftB";

private IvParameterSpec ivParameterSpec;
private SecretKeySpec secretKeySpec;
private Cipher cipher;

public EncryptDecrypt() throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
    ivParameterSpec = new IvParameterSpec(SECRET_KEY_1.getBytes("UTF-8"));
    secretKeySpec = new SecretKeySpec(SECRET_KEY_2.getBytes("UTF-8"), "AES");
    cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
}


public String encrypt(String toBeEncrypt) throws NoSuchPaddingException, NoSuchAlgorithmException,
        InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
    byte[] encrypted = cipher.doFinal(toBeEncrypt.getBytes());
    return Base64.encodeBase64String(encrypted);
}

public String decrypt(String encrypted) throws InvalidAlgorithmParameterException, InvalidKeyException,
        BadPaddingException, IllegalBlockSizeException {
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
    byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encrypted));
    return new String(decryptedBytes);
}
