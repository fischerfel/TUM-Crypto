 final byte[] symKeyData = DatatypeConverter.parseHexBinary(keyHex);
 final byte[] ivData = ivSalt.getBytes(Charset.forName("UTF-8"));
 final byte[] encryptedMessage = DatatypeConverter.parseBase64Binary(encryptedMessageString);
 final Cipher cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding", "BC");
 final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");
 final IvParameterSpec iv = new IvParameterSpec(ivData);
 cipher.init(Cipher.DECRYPT_MODE, symKey, iv);
 final byte[] encodedMessage = cipher.doFinal(encryptedMessage);
 final String message = new String(encodedMessage, Charset.forName("UTF-8"));
