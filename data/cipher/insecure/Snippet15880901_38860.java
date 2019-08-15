byte[] key = hexStringToByteArray("C0C1C2C3C4C5C6C7C8C9CACBCCCDCECF");

byte[] message = hexStringToByteArray("01A0A1A2A3A4A5A6A703020100060001");

SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

// Instantiate the cipher
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

byte[] encrypted = cipher.doFinal(message);

cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
byte[] original = cipher.doFinal(encrypted);
