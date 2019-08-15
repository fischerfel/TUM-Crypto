public static byte[] encrypt(PublicKey pk, byte[] data) throws Exception {
    try {
        Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, pk);
        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(data.length);
        int leavedSize = data.length % blockSize;
        int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
        byte[] raw = new byte[outputSize * blocksSize];
        int i = 0;

        while (data.length - i * blockSize > 0) {
            if (data.length - i * blockSize > blockSize) {
                cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
            } else {
                cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
            }
            i++;
        }

        return raw;
    } catch (Exception e) {
        throw new Exception(e.getMessage());
    }
}
