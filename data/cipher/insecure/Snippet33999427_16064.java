public class Encryption {

public static int MAX_KEY_LENGTH = DESedeKeySpec.DES_EDE_KEY_LEN;
private static String ENCRYPTION_KEY_TYPE = "DESede";
private static String ENCRYPTION_ALGORITHM = "DESede/ECB/NoPadding";
private final SecretKeySpec keySpec; 

public Encryption(String passphrase) {
        byte[] key;
    try {
        // get bytes representation of the password
        key = passphrase.getBytes("UTF8");
    } catch (UnsupportedEncodingException e) {
        throw new IllegalArgumentException(e);
    }

    key = padKeyToLength(key, MAX_KEY_LENGTH);
    keySpec = new SecretKeySpec(key, ENCRYPTION_KEY_TYPE);
}


private byte[] padKeyToLength(byte[] key, int len) {
    byte[] newKey = new byte[len];
    System.arraycopy(key, 0, newKey, 0, Math.min(key.length, len));
    return newKey;
}

// standard stuff
public byte[] encrypt(byte[] unencrypted) throws GeneralSecurityException {
    return doCipher(unencrypted, Cipher.ENCRYPT_MODE);
}

public byte[] decrypt(byte[] encrypted) throws GeneralSecurityException {
    return doCipher(encrypted, Cipher.DECRYPT_MODE);
}

private byte[] doCipher(byte[] original, int mode) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
    int bs = cipher.getBlockSize();
    byte[] padded = new byte[original.length + bs - original.length % bs];
    System.arraycopy(original, 0, padded, 0, original.length);
    cipher.init(mode, keySpec);
    return cipher.doFinal(padded);
}
