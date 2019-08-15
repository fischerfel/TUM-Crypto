import org.bouncycastle.util.encoders.Hex;

public class BadBlowfish {
        private static SecretKey createKey(String theKey) {
        final byte[] keyData = theKey.getBytes(StandardCharsets.US_ASCII);
        final byte[] paddedKeyData = halfPadPKCS7(keyData, 8);
        SecretKey secret = new SecretKeySpec(paddedKeyData, "Blowfish");
        return secret;
    }

    private static byte[] halfUnpadPKCS7(final byte[] paddedPlaintext, int blocksize) {
        int b = paddedPlaintext[paddedPlaintext.length - 1] & 0xFF;
        if (b > 0x07) {
            return paddedPlaintext.clone();
        }
        return Arrays.copyOf(paddedPlaintext, paddedPlaintext.length - b);
    }

    private static byte[] halfPadPKCS7(final byte[] plaintext, int blocksize) {
        if (plaintext.length % blocksize == 0) {
            return plaintext.clone();
        }

        int newLength = (plaintext.length / blocksize + 1) * blocksize;
        int paddingLength = newLength - plaintext.length;

        final byte[] paddedPlaintext = Arrays.copyOf(plaintext, newLength);
        for (int offset = plaintext.length; offset < newLength; offset++) {
            paddedPlaintext[offset] = (byte) paddingLength;
        }
        return paddedPlaintext;
    }

    public static void main(String[] args) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
        SecretKey key = createKey("123456781234567");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plaintextData = cipher.doFinal(Hex.decode("085585C60B3D23257763E6D8BB0A0891"));
        byte[] unpaddedPlaintextData = halfUnpadPKCS7(plaintextData, cipher.getBlockSize());

        String plaintextHex = Hex.toHexString(unpaddedPlaintextData);
        System.out.println(plaintextHex);
        String plaintext = new String(unpaddedPlaintextData, StandardCharsets.UTF_8);
        System.out.println(plaintext);
    }
}
