for (String chunkEncrypted : rsaEcryptedText.getChunkList()) {
    // This works, but I am re-initializing the out cypher every time!
    // super slow!!! WHY DO I HAVE TO DO THIS?
    Cipher outCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", mProvider);
    outCipher.init(Cipher.DECRYPT_MODE, mPrivateKey);
    byte[] cipherText = chunkEncrypted.getBytes("UTF-8");
    CipherInputStream cipherInputStream = new CipherInputStream(
            new ByteArrayInputStream(Base64.decode(cipherText, Base64.NO_WRAP)), outCipher);
    ArrayList<Byte> values = new ArrayList<>();
    int nextByte;
    while ((nextByte = cipherInputStream.read()) != -1) {
        values.add((byte) nextByte);
    }
    byte[] bytes = new byte[values.size()];
    for (int i = 0; i < bytes.length; i++) {
        bytes[i] = values.get(i);
    }
    decryptedString += new String(bytes, 0, bytes.length, "UTF-8");
    cipherInputStream.close();
    cipherInputStream.reset();
}
