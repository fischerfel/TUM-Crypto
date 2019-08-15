Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
cipher.init(Cipher.DECRYPT_MODE, privateKey);
// org.apache.commons.codec.binary.Hex

byte[] cipherText = cipher.doFinal(Hex.decodeHex(encrypted.toCharArray()));
decrypted = new String(cipherText, BaseConstant.ENC_UTF8);
