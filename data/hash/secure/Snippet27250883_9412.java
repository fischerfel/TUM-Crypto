public static byte[] evpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
    int targetKeySize = keySize + ivSize;
    byte[] derivedBytes = new byte[targetKeySize * 4];
    int numberOfDerivedWords = 0;
    byte[] block = null;
    MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
    while (numberOfDerivedWords < targetKeySize) {
        if (block != null) {
            hasher.update(block);
        }
        hasher.update(password);
        block = hasher.digest(salt);
        hasher.reset();

        // Iterations
        for (int i = 1; i < iterations; i++) {
            block = hasher.digest(block);
            hasher.reset();
        }

        System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4,
                Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));

        numberOfDerivedWords += block.length/4;
    }

    System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
    System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);

    return derivedBytes; // key + iv
}
