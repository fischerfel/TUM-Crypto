private static IvParameterSpec generateIV(Cipher cipher) throws Exception {
    byte [] ivBytes = new byte[cipher.getBlockSize()];
    random.nextBytes(ivBytes);    // random = new SecureRandom();
    return new IvParameterSpec(ivBytes);
}

private static SecretKey generateKey(String passphrase) throws Exception {
    PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(), iterations, keyLength);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE_ALGORITHM); //"PBEWITHSHA256AND256BITAES-CBC-BC"
    return keyFactory.generateSecret(keySpec);
}
