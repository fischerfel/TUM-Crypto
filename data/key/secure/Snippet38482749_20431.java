byte[] iv = new byte[16];
Arrays.fill(iv, (byte)0);    

SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
cipher.init(Cipher.ENCRYPT_MODE, key, iv);
byte[] encrypted=cipher.doFinal(msgBytes);
