InputStream payload = new ByteArrayInputStream(payloadArray);
Cipher encryptCipher = Cipher.getInstance("AES", "SunJCE");
encryptCipher.init(Cipher.ENCRYPT_MODE, key, IV);
InputStream encryptStream = new CipherInputStream(payload, encryptCipher);

Cipher decryptCipher = Cipher.getInstance("AES", "SunJCE");
decryptCipher.init(Cipher.DECRYPT_MODE, key, IV);
InputStream decryptStream = new CipherInputStream(encryptStream, decryptCipher);

byte[] plainText = IOUtisl.toByteArray(decryptStream);
