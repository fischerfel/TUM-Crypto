private SecretKey getKey(final String passPhrase, final String salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
    final PBEKeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt.getBytes(), ITERATIONS, 128); // 128bit key length
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_FACTORY_MODE);
    final SecretKey key = keyFactory.generateSecret(keySpec);
    return key;
}
