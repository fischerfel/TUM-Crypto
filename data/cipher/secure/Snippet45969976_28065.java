Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, privKey);
byte[] byteDecryptText = org.apache.commons.codec.binary.Base64.decodeBase64(dataToDecrypt);
decryptedText = cipher.doFinal(byteDecryptText);
decryptedData = new String(decryptedText, "UTF-8");
