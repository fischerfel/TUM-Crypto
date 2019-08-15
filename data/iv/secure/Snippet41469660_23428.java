public class CryptoUtils {

public static byte[] encrypt(byte[] key, byte[] iv, byte[] input) throws GeneralSecurityException {
    try {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        IvParameterSpec ivspec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

        int outputLength = cipher.getOutputSize(input.length);
        byte[] output = new byte[outputLength];
        int outputOffset = cipher.update(input, 0, input.length, output, 0);
        cipher.doFinal(output, outputOffset);

        return output;
    } catch (NoSuchAlgorithmException e) {
        Timber.wtf(e);
    }
    return null;
}

public static byte[] decrypt(byte[] key, byte[] iv, byte[] encrypted) throws GeneralSecurityException {
    try {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

        return cipher.doFinal(encrypted);
    } catch (NoSuchAlgorithmException e) {
        Timber.wtf(e);
    }
    return null;
}
}
