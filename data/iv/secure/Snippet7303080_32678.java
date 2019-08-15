private static final SecureRandom random = new SecureRandom();

private IvParameterSpec getIV(final Cipher cipher) {
    final byte[] ivBytes = new byte[cipher.getBlockSize()];
    random.nextBytes(ivBytes);
    return new IvParameterSpec(ivBytes);
}
