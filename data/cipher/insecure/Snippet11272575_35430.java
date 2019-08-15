Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
