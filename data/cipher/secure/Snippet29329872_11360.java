public class Crypto {

private final String characterEncoding = "UTF-8";
private final String cipherTransformation = "AES/CBC/PKCS5Padding";
private final String aesEncryptionAlgorithm = "AES";

public  byte[] decrypt(byte[] cipherText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
{
    Cipher cipher = Cipher.getInstance(cipherTransformation);
    SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
    IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
    cipherText = cipher.doFinal(cipherText);
    return cipherText;
}

public byte[] encrypt(byte[] plainText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
{
    Cipher cipher = Cipher.getInstance(cipherTransformation);
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
    IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
    plainText = cipher.doFinal(plainText);
    return plainText;
}

private byte[] getKeyBytes(String key) throws UnsupportedEncodingException{
    byte[] keyBytes= new byte[16];
    byte[] parameterKeyBytes= key.getBytes(characterEncoding);
    System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
    return keyBytes;
}

/// <summary>
/// Encrypts plaintext using AES 128bit key and a Chain Block Cipher and returns a base64 encoded string
/// </summary>
/// <param name="plainText">Plain text to encrypt</param>
/// <param name="key">Secret key</param>
/// <returns>Base64 encoded string</returns>
public String encrypt(String plainText, String key) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
    byte[] plainTextbytes = plainText.getBytes(characterEncoding);
    byte[] keyBytes = getKeyBytes(key);
    return Base64.encodeToString(encrypt(plainTextbytes,keyBytes, keyBytes), Base64.DEFAULT);
}

/// <summary>
/// Decrypts a base64 encoded string using the given key (AES 128bit key and a Chain Block Cipher)
/// </summary>
/// <param name="encryptedText">Base64 Encoded String</param>
/// <param name="key">Secret Key</param>
/// <returns>Decrypted String</returns>
public String decrypt(String encryptedText, String key) throws KeyException, GeneralSecurityException, GeneralSecurityException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException{
    byte[] cipheredBytes = Base64.decode(encryptedText, Base64.DEFAULT);
    byte[] keyBytes = getKeyBytes(key);
    return new String(decrypt(cipheredBytes, keyBytes, keyBytes), characterEncoding);
}
