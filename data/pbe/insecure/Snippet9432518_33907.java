String passphrase = ...
byte[] salt = ...
int iterationCount = 12;
String algorithm = "PBEWithMD5AndTripleDES";
KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
SecretKey key = SecretKeyFactory.getInstance(algorithm).generateSecret(keySpec);
Cipher cipher = Cipher.getInstance(key.getAlgorithm());
AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
byte[] encoded = cipher.doFinal(data);
