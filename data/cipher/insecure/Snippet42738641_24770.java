Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, aesKey);
byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));

String encoded = Base64.encodeBase64String(encrypted);
return new String(encoded.getBytes("UTF-8"));
