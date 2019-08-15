public class Cryptor {

private static final String TRANSFORMATION = "AES/CBC/PKCS7Padding";
private static final String AES = "AES";
private static final String RANDOM_ALGO = "SHA1PRNG";
private static final int KEY_LENGTH_IN_BITS = 256;
private static final int IV_LENGTH = 16;
private static final int PBE_ITERATION_COUNT = 5000;
private static final int PBE_SALT_LENGTH_INT_BITS = 128;
private static final String PBE_ALGO = "PBKDF2WithHmacSHA1";


public static byte[] generateKeyFromPassword(String password, int Size) throws GeneralSecurityException {
    byte[] salt = generateSalt();
    KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT, Size);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE_ALGO);
    byte[] data = keyFactory.generateSecret(keySpec).getEncoded();
    return data;
}

private static byte[] generateSalt() throws GeneralSecurityException {
    return randomBytes(PBE_SALT_LENGTH_INT_BITS);
}

private static byte[] randomBytes(int length) throws GeneralSecurityException {
    SecureRandom random = SecureRandom.getInstance(RANDOM_ALGO);
    byte[] b = new byte[length];
    random.nextBytes(b);
    return b;
}

public static byte[] decrypt(byte[] cipherText, String password) throws GeneralSecurityException {
    byte[] keyBytes = generateKeyFromPassword(password, 256);
    byte[] ivBytes = generateKeyFromPassword(password, 128);


    Cipher cipher = Cipher.getInstance(TRANSFORMATION);

    ivBytes = reverse(ivBytes);

    SecretKeySpec secretKey = new SecretKeySpec(keyBytes, AES);
    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

    byte[] decrypted = cipher.doFinal(cipherText);
    return decrypted;
}


public static byte[] reverse(byte[] array) {
    if (array == null) {
        return null;
    }
    int i = 0;
    int j = array.length - 1;
    byte tmp;
    while (j > i) {
        tmp = array[j];
        array[j] = array[i];
        array[i] = tmp;
        j--;
        i++;
    }
    return array;
}
