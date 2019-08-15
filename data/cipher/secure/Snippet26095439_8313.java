private static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

cipherText = cipher.doFinal(stuffIWantSafe.getBytes("UTF-8"));

String encodedCipherText = Base64.encodeToString(cipherText, Base64.NO_WRAP);
