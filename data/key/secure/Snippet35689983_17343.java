Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
byte[] key = getKey(); \\Assume it is implemented.
byte[] iv = getIv(); \\Assume it is implemented;
SecretKeySpec sc = new SecretKeySpec(key, "AES");
cipher.init(Cipher.DECRYPT_MODE, sc, new IvParameterSpec(iv));
byte[] encrypted = getBytesFromFile(); \*Assume it is implemented. Simply reads bytes from a binary file into a byte array and returns them as are.*\
byte[] clear = new byte[cipher.getOutputSize(encrypted.length)];
int processed = cipher.doFinal(encrypted, 0, encrypted.length, clear, 0);
