// generate a SHA-1 hash from your key to get 128 bit key
byte[] key = (SALT2 + "yoursecret key").getBytes("UTF-8");
MessageDigest sha = MessageDigest.getInstance("SHA-1");
key = sha.digest(key);
key = Arrays.copyOf(key, 16); // use only first 128 bit

String key = new String(org.apache.commons.codec.binary.Hex.encodeHex(raw));
byte[] keyByteArray = org.apache.commons.codec.binary.Hex.decodeHex(key.toCharArray());
SecretKeySpec skeySpec = new SecretKeySpec(keyByteArray, "AES");

Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
byte[] encrypted = cipher.doFinal("Username / Password or some secret text".getBytes());
String encryptedMessage = new String(org.apache.commons.codec.binary.Hex.encodeHex(encrypted));
System.out.printf("The encrypted message: %s\n", encryptedMessage);
