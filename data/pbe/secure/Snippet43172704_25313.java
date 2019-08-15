byte[] iv = new byte[16];
byte[] salt = new byte[32];
byte[] ctChunk = new byte[8192]; // not for whole ciphertext, just a buffer

if (16 != fileInputStream.read(iv) || 32 != fileInputStream.read(salt)) {
    throw new Exception("IV or salt too short");
}

KeySpec keySpec = new PBEKeySpec(submittedPassword.toCharArray(), salt, 1000, 256);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
SecretKey key = new SecretKeySpec(keyBytes, "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
IvParameterSpec ivParams = new IvParameterSpec(iv);
cipher.init(Cipher.DECRYPT_MODE, key, ivParams);

int read;
ByteArrayOutputStream ctBaos = new ByteArrayOutputStream();
while((read = fileInputStream.read(ctChunk)) > 0) {
    ctBaos.write(cipher.update(cipherText, 0, read));
}
ctBaos.write(cipher.doFinal());

String plainrStr = new String(ctBaos.toByteArray(), "UTF-8");
textEdit.setText(plainrStr);
