Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
c.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(zeroBytes));

byte[] encrypted_response = transceive(c.doFinal(message));

// TODO: cipher needs to be re-set to DECRYPT_MODE while retaining the IV
c.init(Cipher.DECRYPT_MODE, ...?);

byte[] response = c.doFinal(encrypted_response);
