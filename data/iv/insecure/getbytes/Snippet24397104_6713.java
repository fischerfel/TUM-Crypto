String IV = "AAAAAAAAAAAAAAAA"; // generate this randomly
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(IV.getBytes()));
byte[] encrypted = cipher.doFinal(s.getBytes());
