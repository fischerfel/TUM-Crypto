String key = "abcd";
SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
Cipher cipher = Cipher.getInstance("Blowfish");
cipher.init(cipher.ENCRYPT_MODE, keySpec);
return DatatypeConverter.printBase64Binary(cipher.doFinal(key.getBytes()));
