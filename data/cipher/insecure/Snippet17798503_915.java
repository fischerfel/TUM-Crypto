public static final byte[] ige(final byte[] key, final byte[] IV,
        final byte[] Message) throws Exception {

    final Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));

    final int blocksize = cipher.getBlockSize();

    byte[] xPrev = Arrays.copyOfRange(IV, 0, blocksize);
    byte[] yPrev = Arrays.copyOfRange(IV, blocksize, IV.length);

    byte[] decrypted = new byte[0];

    byte[] y, x;
    for (int i = 0; i < Message.length; i += blocksize) {
        x = java.util.Arrays.copyOfRange(Message, i, i + blocksize);
        y = xor(cipher.doFinal(xor(x, yPrev)), xPrev);
        xPrev = x;
        yPrev = y;

        decrypted = sumBytes(decrypted, y);
    }

    return decrypted;
}
