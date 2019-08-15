encrypted = encrypt("201412181656005P443m2Q1R9A7f5r3e1z08642","5P443m2Q1R9A7f5r3e1z08642");

public class Crypt {

    private final String characterEncoding = "UTF-8";
    private final String cipherTransformation = "AES/ECB/PKCS5Padding";
    private final String aesEncryptionAlgorithm = "AES";

    public  byte[] decrypt(byte[] cipherText, byte[] key) throws Exception
    {
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy);
        cipherText = cipher.doFinal(cipherText);
        return cipherText;
    }

    public byte[] encrypt(byte[] plainText, byte[] key) throws Exception
    {
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        plainText = cipher.doFinal(plainText);
        return plainText;
    }

    private byte[] getKeyBytes(String key) throws UnsupportedEncodingException{
        byte[] keyBytes= new byte[16];
        byte[] parameterKeyBytes= key.getBytes(characterEncoding);
        System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
        return keyBytes;
    }

    @SuppressLint("NewApi")
    public String encrypt(String plainText, String key) throws Exception {
        byte[] plainTextbytes = plainText.getBytes(characterEncoding);
        byte[] keyBytes = getKeyBytes(key);
        // Log.i("iv", ""+keyBytesIV);
        return Base64.encodeToString(encrypt(plainTextbytes,keyBytes), Base64.DEFAULT);
    }

    @SuppressLint("NewApi")
    public String decrypt(String encryptedText, String key) throws Exception {
        byte[] cipheredBytes = Base64.decode(encryptedText, Base64.DEFAULT);
        byte[] keyBytes = getKeyBytes(key);

        return new String(decrypt(cipheredBytes, keyBytes), characterEncoding);
    }

}
