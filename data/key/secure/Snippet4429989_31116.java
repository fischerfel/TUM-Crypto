private byte[] encryptAES128(byte[] input, byte[] initialVector) {
    SecretKey aeskey = new SecretKeySpec(keyC, 0, 16, "AES");
    AlgorithmParameterSpec paramSpec = new IvParameterSpec(initialVector);
    cipher = Cipher.getInstance("AES/CTR/NOPADDING");
    getAesCTRCipher().init(Cipher.ENCRYPT_MODE, aeskey, paramSpec);
    return getAesCTRCipher().doFinal(input);
}
