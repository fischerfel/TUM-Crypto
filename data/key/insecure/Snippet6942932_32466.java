// 16 characters for 128-bit AES
private final static String PASSPHRASE="myappspassphrase"

SecretKeySpec key = new SecretKeySpec(PASSPHRASE.getBytes("UTF-8"), "AES");

...

// Initialise the cipher
Cipher encryptCipher = Cipher.getInstance("AES");
encryptCipher.init(Cipher.ENCRYPT_MODE, key);
