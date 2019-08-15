private static final int AES_BLOCK_SIZE = 16;
private static IvParameterSpec calculateIVForOffset(final IvParameterSpec iv,
    final long blockOffset) {
final BigInteger ivBI = new BigInteger(1, iv.getIV());
final BigInteger ivForOffsetBI = ivBI.add(BigInteger.valueOf(blockOffset
        / AES_BLOCK_SIZE));

final byte[] ivForOffsetBA = ivForOffsetBI.toByteArray();
final IvParameterSpec ivForOffset;
if (ivForOffsetBA.length >= AES_BLOCK_SIZE) {
    ivForOffset = new IvParameterSpec(ivForOffsetBA, ivForOffsetBA.length - AES_BLOCK_SIZE,
            AES_BLOCK_SIZE);
} else {
    final byte[] ivForOffsetBASized = new byte[AES_BLOCK_SIZE];
    System.arraycopy(ivForOffsetBA, 0, ivForOffsetBASized, AES_BLOCK_SIZE
            - ivForOffsetBA.length, ivForOffsetBA.length);
    ivForOffset = new IvParameterSpec(ivForOffsetBASized);
}

return ivForOffset;
}
