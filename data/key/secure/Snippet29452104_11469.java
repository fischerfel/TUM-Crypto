byte[] ciphertextBytes = Base64.decode(cipherText, Base64.DEFAULT);
IvParameterSpec iv = new IvParameterSpec(ciphertextBytes, 0, 16);
ciphertextBytes = Arrays.copyOfRange(ciphertextBytes, 16, ciphertextBytes.length);

SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");          
cipher.init(Cipher.DECRYPT_MODE, key, iv);
String decrypt = new String(cipher.doFinal(ciphertextBytes), "UTF-8");
