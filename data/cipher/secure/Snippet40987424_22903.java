Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

Cipher outCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
outCipher.init(Cipher.DECRYPT_MODE, privateKey);

ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
CipherOutputStream cipherOutputStream = new CipherOutputStream(
        outputStream, inCipher);
cipherOutputStream.write(plainText.getBytes("UTF-8"));
cipherOutputStream.close();

String ecryptedText = outputStream.toString();
Log.d(TAG, "Encrypt = " + ecryptedText);

String cipherText = ecryptedText;
CipherInputStream cipherInputStream = new CipherInputStream(
        new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), outCipher);
ArrayList<Byte> values = new ArrayList<>();
int nextByte;
while ((nextByte = cipherInputStream.read()) != -1) {
    values.add((byte)nextByte);
}

byte[] bytes = new byte[values.size()];
for(int i = 0; i < bytes.length; i++) {
    bytes[i] = values.get(i).byteValue();
}

String finalText = new String(bytes, 0, bytes.length, "UTF-8");
Log.d(TAG, "Decrypt = " + ecryptedText);
