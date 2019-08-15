// zeros by default
byte[] rawKey = new byte[32];
// if you don't specify the encoding you might get weird results
byte[] keyBytes = AES_SECRET.getBytes("ASCII");
System.arraycopy(keyBytes, 0, rawKey, 0, keyBytes.length);
SecretKey key = new SecretKeySpec(rawKey, "AES");
Cipher cipher = ...
// rest of your decryption code
