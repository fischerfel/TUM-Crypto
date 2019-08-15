public class SecureEncryption {

private static final String CONTENT = "thisneedstobestoredverysecurely";
private static final String PASSPHRASE = "mysuperstrongpassword";
private static final int IV_LENGTH = 16;

public static void main(String[] args) throws Exception {
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
    byte[] passphrase = digest.digest(PASSPHRASE.getBytes("UTF-8"));

    Cipher instance = Cipher.getInstance("AES/CFB/NoPadding");
    passphrase = Arrays.copyOf(passphrase, 16);
    SecretKeySpec secretKey = new SecretKeySpec(passphrase, "AES");
    byte[] iv = new byte[16];
    SecureRandom sr = new SecureRandom();
    sr.nextBytes(iv);
    instance.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
    byte[] encrypted = instance.doFinal(CONTENT.getBytes("UTF-8"));
    byte[] result = addIVtoEncrypted(iv, encrypted);

    System.arraycopy(result, 0, iv, 0, IV_LENGTH);
    System.arraycopy(result, IV_LENGTH, encrypted, 0, result.length - IV_LENGTH);
    instance.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
    byte[] decrypted = instance.doFinal(encrypted);
    System.out.println(new String(decrypted, "UTF-8"));
}

private static byte[] addIVtoEncrypted(byte[] iv, byte[] encrypted) {
    byte[] ret = new byte[IV_LENGTH + encrypted.length];
    System.arraycopy(iv, 0, ret, 0, IV_LENGTH);
    System.arraycopy(encrypted, 0, ret, IV_LENGTH, encrypted.length);
    return ret;
}
}
