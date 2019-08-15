Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
// By doing this here w/o an IvParameterSpec, you let the
// cipher initialization create it. Less chance (see what I did there)
// to influence values that should be completely random.
cipher.init(cipherMode, secretKey);
AlgorithmParameters ivSpec = cipher.getParameters();
byte[] nonceAndCounter= ivSpec.getEncoded()
