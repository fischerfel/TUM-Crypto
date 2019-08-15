Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
IvParameterSpec ivspec = new IvParameterSpec(initialztnVector);
cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
byte[] enc = cipher.doFinal(toEncrypt);
