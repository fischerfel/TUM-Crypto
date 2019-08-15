public class EncryptionHelper {

// Encrypts string and encode in Base64
public static String encryptText(String plainText,String key, String IV) throws Exception {
    // ---- Use specified 3DES key and IV from other source --------------
    byte[] plaintext = plainText.getBytes();//input
    byte[] tdesKeyData = key.getBytes();// your encryption key

    byte[] myIV = IV.getBytes();// initialization vector

    Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
    IvParameterSpec ivspec = new IvParameterSpec(myIV);

    c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
    byte[] cipherText = c3des.doFinal(plaintext);
    String encryptedString = Base64.encodeToString(cipherText,
            Base64.DEFAULT);
    // return Base64Coder.encodeString(new String(cipherText));
    return encryptedString;
}
