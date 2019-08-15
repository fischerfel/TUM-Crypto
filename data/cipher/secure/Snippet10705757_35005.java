SecretKeySpec   key = new SecretKeySpec(KEY.getBytes(), "AES");
IvParameterSpec iv  = new IvParameterSpec(IV.getBytes());

Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, key, iv);

byte[] output = cipher.doFinal(cleartext.getBytes());

String signature = Base64.encode(output);
