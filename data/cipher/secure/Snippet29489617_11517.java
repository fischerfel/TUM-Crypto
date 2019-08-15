Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.WRAP_MODE, rsaKeyPair.getPublic());
byte[] wrappedKey = cipher.doFinal(keyData);
