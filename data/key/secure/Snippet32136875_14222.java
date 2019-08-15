String keyHex = new String(readFileToByteArray(file));
byte[] encoded = decodeHex(keyHex.toCharArray());
SecretKey myKey = new SecretKeySpec(encoded, "AES");
