Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, sKey);
String ivBase64 = Base64.encodeBytes(cipher.getParameters().getEncoded());
