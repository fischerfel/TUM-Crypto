SecretKey k1 = generateDESkey();
SecretKey k2 = generateDESkey();

byte[] edeKeyBytes = new byte[24];
System.arraycopy(k1.getEncoded(), 0, edeKeyBytes, 0, 8);
System.arraycopy(k2.getEncoded(), 0, edeKeyBytes, 8, 8);
System.arraycopy(k1.getEncoded(), 0, edeKeyBytes, 16, 8);

edeKey = new SecretKeySpec(edeKeyBytes, "DESede");

Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, edeKey);

System.out.println(Base64.encode(cipher.doFinal("plaintext".getBytes("UTF-8"))));
