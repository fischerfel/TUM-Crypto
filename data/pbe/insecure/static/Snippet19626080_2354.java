public class SecureEncryption {

private static final String CONTENT = "thisneedstobestoredverysecurely";
private static final String PASSPHRASE = "mysuperstrongpassword";
private static final int IV_LENGTH = 16;
private static final int AES_KEY_LENGTH = 16;
private static final int MAC_KEY_LENGTH = 16;
private static final int MAC_LENGTH = 20;
private static final int ITERATION_COUNT = 4096;
private static final String AES = "AES";
private static final String CIPHER_ALGORITHM = "AES/CFB/NoPadding";
private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
private static final String MAC_ALGORITHM = "HmacSHA1";

public static void main(String[] args) throws Exception {
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    SecureRandom sr = new SecureRandom();
    byte[] salt = new byte[16];
    sr.nextBytes(salt);

    SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
    SecretKey secretKey = factory.generateSecret(new PBEKeySpec(PASSPHRASE.toCharArray(), salt, ITERATION_COUNT, 256));
    byte[] secretBytes = secretKey.getEncoded();

    byte[] iv = new byte[16];
    sr.nextBytes(iv);
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretBytes, 0, AES_KEY_LENGTH, AES), new IvParameterSpec(iv));
    byte[] encrypted = cipher.doFinal(CONTENT.getBytes("UTF-8"));
    byte[] result = concatArrays(iv, encrypted);

    byte[] macResult = getMAC(secretBytes, result);
    result = concatArrays(macResult, result);

    System.arraycopy(result, 0, macResult, 0, MAC_LENGTH);
    System.arraycopy(result, MAC_LENGTH, iv, 0, IV_LENGTH);
    System.arraycopy(result, MAC_LENGTH + IV_LENGTH, encrypted, 0, result.length - IV_LENGTH - MAC_LENGTH);

    if (!Arrays.equals(getDigest(getMAC(secretBytes, concatArrays(iv, encrypted))), getDigest(macResult))) {
        System.out.println("Invalid MAC");
    }
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretBytes, 0, AES_KEY_LENGTH, AES), new IvParameterSpec(iv));
    byte[] decrypted = cipher.doFinal(encrypted);
    System.out.println(new String(decrypted, "UTF-8"));
}

private static byte[] getDigest(byte[] mac) throws Exception {
    MessageDigest digest = MessageDigest.getInstance("SHA1");
    return digest.digest(mac);
}

private static byte[] getMAC(byte[] secretBytes, byte[] data) throws Exception {
    Mac mac = Mac.getInstance(MAC_ALGORITHM);
    mac.init(new SecretKeySpec(secretBytes, AES_KEY_LENGTH, MAC_KEY_LENGTH, MAC_ALGORITHM));
    return mac.doFinal(data);
}

private static byte[] concatArrays(byte[] first, byte[] second) {
    byte[] ret = new byte[first.length + second.length];
    System.arraycopy(first, 0, ret, 0, first.length);
    System.arraycopy(second, 0, ret, first.length, second.length);
    return ret;
}
}
