// Algorithm, mode and padding must match encryption.
// Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

// If you have Bouncycastle library installed, you can use
// Rijndael/CBC/PKCS7PADDING directly.
Cipher cipher = Cipher.getInstance("Rijndael/CBC/PKCS7PADDING");

// convertedSecureString and initVector must be byte[] with correct length
cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(convertedSecureString, "AES"),
    new IvParameterSpec(initVector));

CipherInputStream cryptoStream = new CipherInputStream(inputStream, cipher);
byte[] buffer = new byte[1024];
int len = cryptoStream.read(buffer, 0, buffer.length);
while (len > 0) {
    outputStream.write(buffer, 0, len);
    len = cryptoStream.read(buffer, 0, buffer.length);
}

outputStream.flush();
cryptoStream.close();
// no need to close inputStream here, as cryptoStream will close it
