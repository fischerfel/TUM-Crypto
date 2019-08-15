byte[] kSession= fileToBytes(kSessionFile);
SecretKeySpec skeySpec = new SecretKeySpec(kSession, "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
