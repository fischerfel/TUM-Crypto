private static Key deriveSecretKey(String secretKeyAlgorithm, String secretKey, String salt) throws Exception {

  SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
  KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), char2byte(salt), 65536, 128);
  SecretKey tmp = factory.generateSecret(spec);
  SecretKey secret = new SecretKeySpec(tmp.getEncoded(), secretKeyAlgorithm);

  return secret;
}
