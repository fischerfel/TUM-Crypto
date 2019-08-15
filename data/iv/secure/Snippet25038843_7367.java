byte[] iv = new byte[16]; // must be 16 bytes for AES-128
new SecureRandom().nextBytes(iv); // generate random bytes
IvParameterSpec ivSpec = new IvParameterSpec(iv);

/* create instance of Cipher and keys */

cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
