public class BouncyLightWeightCipherExample {

    private static final boolean FOR_DECRYPTION = false;

    public static void main(String[] args) throws NoSuchAlgorithmException, Exception {
        final byte[] keyData = new byte[256 / Byte.SIZE];
        final byte[] ivData = new byte[16];
        final byte[] testData = "owlstead".getBytes(UTF_8);

        // JCE creation
        final Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // initialization
        c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyData, "AES"), new IvParameterSpec(ivData));

        // and encryption
        final byte[] ciphertext = c.doFinal(testData);

        // Bouncy Castle creation
        final BlockCipher blockCipher = new AESFastEngine();
        final CBCBlockCipher withModeOfOperation = new CBCBlockCipher(blockCipher);
        final PaddedBufferedBlockCipher withPadding = new PaddedBufferedBlockCipher(withModeOfOperation);

        // initialization
        final ParametersWithIV keyAndIV = new ParametersWithIV(new KeyParameter(keyData), ivData);
        withPadding.init(FOR_DECRYPTION, keyAndIV);

        // and decryption
        int plaintextSize = withPadding.processBytes(ciphertext, 0, ciphertext.length, ciphertext, 0);
        plaintextSize += withPadding.doFinal(ciphertext, plaintextSize);
        final byte[] plaintext = Arrays.copyOf(ciphertext, plaintextSize);

        // there we are
        System.out.println(new String(plaintext, UTF_8));
    }
}
