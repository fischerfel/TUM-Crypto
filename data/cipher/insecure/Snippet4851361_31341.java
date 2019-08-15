// Create Key
DESKeySpec desKeySpec = new DESKeySpec(key);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

// Create Cipher
Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
