// My user name
byte[] loginId = "login".getBytes();

byte[] preSharedKey128 = "ACME-1234AC".getBytes();
byte[] preSharedKey192 = "ACME-1234ACME-1234A".getBytes();
// 256 bit key
byte[] preSharedKey256 = "ACME-1234ACME-1234ACME-1234".getBytes();
byte[] preSharedKey = preSharedKey256;

// Initialization Vector
// Required for CBC
byte[] iv ={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
IvParameterSpec ips = new IvParameterSpec(iv);


byte[] encodedKey = new byte[loginId.length + preSharedKey.length];

System.arraycopy(loginId, 0, encodedKey, 0, loginId.length);
System.arraycopy(preSharedKey, 0, encodedKey, loginId.length, preSharedKey.length);

// The SecretKeySpec provides a mechanism for application-specific generation
// of cryptography keys for consumption by the Java Crypto classes.

// Create a key specification first, based on our key input.
SecretKey aesKey = new SecretKeySpec(encodedKey, "AES");

// Create a Cipher for encrypting the data using the key we created.
Cipher encryptCipher;

encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
// Initialize the Cipher with key and parameters
encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey, ips);

// Our cleartext
String clearString = "33,8244000,9999,411,5012022517,0.00,0,1,V330";
byte[] cleartext = clearString.getBytes();

// Encrypt the cleartext
byte[] ciphertext = encryptCipher.doFinal(cleartext);

// Now decrypt back again...
// Decryption cipher
Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
// Initialize PBE Cipher with key and parameters
decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ips);

// Decrypt the cleartext
byte[] deciphertext = decryptCipher.doFinal(ciphertext);
