localCipher = Cipher.getInstance("AES/CBC/NoPadding", "SC");
localCipher.init(2, new SecretKeySpec(arrayOfByte1, "AES"), new IvParameterSpec(arrayOfByte2));
byte[] resultarray = new CipherInputStream(cipherDataf, localCipher);
