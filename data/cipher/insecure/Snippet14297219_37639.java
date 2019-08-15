SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
byte[] encrypted = cipher.doFinal(input.getBytes());
