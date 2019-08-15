final SecretKey key = getKey(passPhrase, salt);
final Cipher cipher = Cipher.getInstance("AES/CTR/NOPADDING");
final IvParameterSpec iv = getIV(cipher);
cipher.init(cipherMode, key, iv, random);
