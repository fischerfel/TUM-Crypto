Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

byte[] ivBytes = new byte[c.getBlockSize()];
String IV = CryptoUtils.hexEncode(ivBytes);
