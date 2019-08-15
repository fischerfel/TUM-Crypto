final IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
final SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

final Cipher cipherSpec = Cipher.getInstance("AES/CBC/PKCS5PADDING");
cipherSpec.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
cipherSpec.doFinal(DatatypeConverter.parseBase64Binary(encrypted));
