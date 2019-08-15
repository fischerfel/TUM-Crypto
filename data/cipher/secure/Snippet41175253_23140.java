Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", mProvider);
inCipher.init(Cipher.ENCRYPT_MODE, mPublicKey);
RSAString rsaStringPlainText = new RSAString(plainText);
for (String chunkPlain : rsaStringPlainText.getChunkList()) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CipherOutputStream cipherOutputStream = new CipherOutputStream(
            outputStream, inCipher);
    cipherOutputStream.write(chunkPlain.getBytes("UTF-8"));
    cipherOutputStream.flush();
    cipherOutputStream.close();
    byte[] ecryptedText = Base64.encode(outputStream.toByteArray(), Base64.NO_WRAP);
    encryptedStringOut.mChunkList.add(new String(ecryptedText));
}
