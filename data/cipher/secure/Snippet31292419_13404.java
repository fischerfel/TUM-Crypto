public class Cryptography {

    /**
     * @param key           AES Key
     * @param inputValue    Data to encrypt
     * @return Can return null if something goes wrong
     */
    public static byte[] encryptAES(byte[] key, byte[] inputValue)
            throws NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException
    {
        SecretKeySpec sKeyS = new SecretKeySpec(key, "AES");

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, sKeyS);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException i) {
            cipher = null;
        }

        return cipher != null ? cipher.doFinal(inputValue) : null;
    }

    public static byte[] decryptAES(byte[] key, byte[] encryptedData)
            throws NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException
    {
        SecretKeySpec sKeyS = new SecretKeySpec(key, "AES");

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, sKeyS);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException i) {
            cipher = null;
        }

        return cipher != null ? cipher.doFinal(encryptedData) : null;
    }

    private static byte[] deriveAES256KeySalt = null;
    public static byte[] deriveAES256Key(String password)
            throws InvalidKeySpecException, NoSuchAlgorithmException
    {

    /* Store these things on disk used to derive key later: */
        int iterationCount = 1000;
        int saltLength = 32; // bytes; should be the same size as the output (256 / 8 = 32)
        int keyLength = 256; // 256-bits for AES-256, 128-bits for AES-128, etc

    /* When first creating the key, obtain a salt with this: */
    if(deriveAES256KeySalt == null) {
        SecureRandom random = new SecureRandom();
        deriveAES256KeySalt = new byte[saltLength];
        random.nextBytes(deriveAES256KeySalt);
    }

    /* Use this to derive the key from the password: */
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), deriveAES256KeySalt, iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();

        return keyBytes;
    }
}
