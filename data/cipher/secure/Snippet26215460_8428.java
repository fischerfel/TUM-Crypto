public Cipher getDownloadCipher(final long startPosition) throws Exception {
    final Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
    final ByteBuffer buffer = ByteBuffer.allocate(16).put(nonce);
    buffer.asLongBuffer().put(startPosition / 16);
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(buffer.array()));
    final int skip = (int) (startPosition % 16);
    if (skip != 0) {
        if (cipher.update(new byte[skip]).length != skip) {
            //that should always work with a CTR mode cipher
            throw new IOException("Failed to skip bytes from cipher");
        }
    }
    return cipher;
}
