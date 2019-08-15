Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
cipher.init(1, new SecretKeySpec(theKey.getBytes(), "BlowFish"));
byte[] arrayOfByte = cipher.doFinal(inputString.getBytes());
String result = new BASE64Encoder().encode(arrayOfByte);
