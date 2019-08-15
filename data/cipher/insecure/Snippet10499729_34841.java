Cipher desCipher = Cipher.getInstance("DES/CBC/NoPadding");
DESKeySpec desSpec = new DESKeySpec(desKey);
SecretKey des = SecretKeyFactory.getInstance("DES").generateSecret(desSpec);
desCipher.init(Cipher.ENCRYPT_MODE, des , ivSpec);
