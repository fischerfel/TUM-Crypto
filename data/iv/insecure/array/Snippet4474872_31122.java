public class encryption {
public static final String TAG = "smsfwd"
private static Cipher aesCipher;
private static SecretKey secretKey;
private static IvParameterSpec ivParameterSpec;

private static String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
private static String CIPHER_ALGORITHM = "AES";
//the secret key in HEX is 'secretkey'
private static byte[] rawSecretKey = {Ox73, Ox65, Ox63, Ox72, Ox65, Ox74, Ox6B, Ox65, Ox79};

private static String MESSAGEDIGEST_ALGORITHM = "MD5";

public encryption(String passphrase) {
    byte[] passwordKey = encodeDigest(passphrase);

    try {
        aesCipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
    } catch (NoSuchAlgorithmException e) {
        Log.e(TAG, "No such algorithm " + CIPHER_ALGORITHM, e);
    } catch (NoSuchPaddingException e) {
        Log.e(TAG, "No such padding PKCS5", e);
    }

    secretKey = new SecretKeySpec(passwordKey, CIPHER_ALGORITHM);
    ivParameterSpec = new IvParameterSpec(rawSecretKey);
}




//base 64 encryption
public String encryptAsBase64(byte[] clearData) {
    byte[] encryptedData = encrypt(clearData);
    return base64.encodeBytes(encryptedData);
}




public byte[] encrypt(byte[] clearData) {
    try {
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
    } catch (InvalidKeyException e) {
        Log.e(TAG, "Invalid key", e);
        return null;
    } catch (InvalidAlgorithmParameterException e) {
        Log.e(TAG, "Invalid algorithm " + CIPHER_ALGORITHM, e);
        return null;
    }

    byte[] encryptedData;
    try {
        encryptedData = aesCipher.doFinal(clearData);
    } catch (IllegalBlockSizeException e) {
        Log.e(TAG, "Illegal block size", e);
        return null;
    } catch (BadPaddingException e) {
        Log.e(TAG, "Bad padding", e);
        return null;
    }
    return encryptedData;
}

private byte[] encodeDigest(String text) {
    MessageDigest digest;
    try {
        digest = MessageDigest.getInstance(MESSAGEDIGEST_ALGORITHM);
        return digest.digest(text.getBytes());
    } catch (NoSuchAlgorithmException e) {
        Log.e(TAG, "No such algorithm " + MESSAGEDIGEST_ALGORITHM, e);
    }

    return null;
}
