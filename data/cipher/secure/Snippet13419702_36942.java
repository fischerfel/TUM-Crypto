SecretKeySpec spec = new SecretKeySpec(
    token.getRemoteEncryptingKey(),
    "AES");

cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(token.getRemoteInitializationVector()));
decryptedBytes = cipher.update(dataToDecrypt, inputOffset,
    inputLength, output, outputOffset);
decryptedBytes += cipher.doFinal(output, outputOffset+decryptedBytes);
return decryptedBytes;
