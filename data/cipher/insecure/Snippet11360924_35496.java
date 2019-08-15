Key key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(bytesOfThe Key)); // bytesOfTheKey should be 8 bytes long
Cipher cipher = Cipher.getInstance("DES");
cipher.init(Cipher.DECRYPT_MODE, key);
return new CipherInputStream(inputStream, cipher);
