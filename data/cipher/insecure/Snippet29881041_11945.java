private final String CIPHER_NAME = "AES/ECB/PKCS5Padding";
private final String ALGORITHM_NAME = "AES"; // keySizes 128, 192, 256
// private final String CIPHER_NAME = "DES/ECB/PKCS5Padding";
// private final String ALGORITHM_NAME = "DES"; // keySize 56
// private final String CIPHER_NAME = "DESede/ECB/PKCS5Padding";
// private final String ALGORITHM_NAME = "DESede"; // keySize 168

byte[] encrypt(SecretKey key, long num) {
    BigInteger bignum = BigInteger.valueOf(num);
    Cipher cipher = Cipher.getInstance(CIPHER_NAME);
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return cipher.doFinal(bignum.toByteArray());
}

long decrypt(SecretKey key, byte[] ct) {
    Cipher cipher = Cipher.getInstance(CIPHER_NAME);
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] pt = cipher.doFinal(ct);
    BigInteger bignum = new BigInteger(pt);
    return bignum.longValue();
}

SecretKey keyGen(String algorithm, int keySize) {
    KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
    keygen.init(keySize);
    return keygen.generateKey();
}
