decryptCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(cipher.getIV(), 0, 16));
//Get the array after the 7 bytes from the header
byte[] encrypted = Arrays.copyOfRange(sbResponse.toString().getBytes(), 7, sbResponse.toString().length());
