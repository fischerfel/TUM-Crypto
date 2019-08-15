private static String   IV              = "AAAAAAAAAAAAAAAA";
private static String   ENCRYPTION_KEY  = "0123456789abcdef";

Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes("UTF-8"), "AES");
cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
return cipher.doFinal(input.getBytes("UTF-8"));
