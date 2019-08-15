encryptionKey   = "Omnia Gallia in tres partes divida est";
byte[] newValue = new byte[24];
System.arraycopy(encryptionKey, 0, newValue, 0, 24);
encryptionKey   = newValue;

KeySpec keySpec             = new DESedeKeySpec(encryptionKey);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESEde");
Cipher cipher               = Cipher.getInstance("DESEde");    
SecretKey key               = keyFactory.generateSecret(keySpec);

cipher.init(Cipher.ENCRYPT_MODE, key);

cipherBytes = cipher.doFinal(plainBytes);
