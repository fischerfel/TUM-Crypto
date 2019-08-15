SecretKeySpec keyspec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));

Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));

// Then I convert encrypted to hex by building a string of encrypted[i] & 0xFF
