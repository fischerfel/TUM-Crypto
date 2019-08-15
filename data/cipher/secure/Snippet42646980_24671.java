 byte[] b = new byte[40000];
 new Random().nextBytes(b);

 Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
 cipher.init(Cipher.ENCRYPT_MODE, key);

 byte[] first = cipher.doFinal(b);
