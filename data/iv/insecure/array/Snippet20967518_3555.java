String salt = "This is my test";
byte[] saltBytes = salt.getBytes("UTF-8");
byte[] ivBytes = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 0x07,
    0x72, 0x6F, 0x5A, (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 0x07, 0x72,
    0x6F, 0x5A };
SecretKeyFactory factory = SecretKeyFactory
    .getInstance("PBKDF2WithHmacSHA1");
PBEKeySpec spec = new PBEKeySpec(encryptionKey.toCharArray(), saltBytes,
    iterationCount, keySize);

SecretKey secretKey = factory.generateSecret(spec);
SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

encryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
encryptionCipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(ivBytes));

decryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
decryptionCipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));
