SecretKeySpec skeySpec = new SecretKeySpec(keyText.getBytes(), "DES");
Cipher des = Cipher.getInstance("DES/CBC/ZeroBytePadding", "BC");
des.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[8]));
byte[] tokenData = des.doFinal(Base64.decodeBase64(token));
