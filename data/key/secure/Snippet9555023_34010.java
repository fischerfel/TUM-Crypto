KeyGenerator kgen = KeyGenerator.getInstance("AES");
int keySize = 128;
kgen.init(keySize);
SecretKey key = kgen.generateKey();
byte[] aesKey = key.getEncoded();
SecretKeySpec aesKeySpec = new SecretKeySpec(aesKey, "AES");
Cipher aesCipher = Cipher.getInstance("AES");
aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec);
byte[] encryptedContent = aesCipher.doFinal(content);
