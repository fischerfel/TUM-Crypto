byte[] keyValue = new byte[]{(byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x16,(byte) 0x05, (byte) 0x12};
myKeySpec = new DESKeySpec(keyValue);
mySecretKeyFactory = SecretKeyFactory.getInstance("DES");
key = mySecretKeyFactory.generateSecret(myKeySpec);


Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
IvParameterSpec iv2 = new IvParameterSpec(new byte[8]);
cipher.init(Cipher.ENCRYPT_MODE, key, iv2);
byte[] plainText = function.HexStringToByteArray(payloadRecv);
byte[] encryptedText = cipher.doFinal(plainText);
