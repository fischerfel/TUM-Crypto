// bytes to encrypt
byte[] input;

// the key and the initialization vector
byte[] keyBytes;
byte[] ivBytes;

// initialize the Cipher
SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

// encryption
cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
byte[] encrypted= new byte[cipher.getOutputSize(input.length)];
int enc_len = cipher.update(input, 0, input.length, encrypted, 0);
enc_len += cipher.doFinal(encrypted, enc_len);

// decryption
cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
byte[] decrypted = new byte[cipher.getOutputSize(enc_len)];
int dec_len = cipher.update(encrypted, 0, enc_len, decrypted, 0);
dec_len += cipher.doFinal(decrypted, dec_len);
