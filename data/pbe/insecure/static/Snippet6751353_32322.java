SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithMD5AndAES");
KeySpec spec = new PBEKeySpec("asdasd".toCharArray(), new String("Ip/6nf5p4Cvg4uocLdIeHJ7uW/Y=").getBytes(), 162752, 192);
SecretKey tmp = factory.generateSecret(spec);
SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, secret);
