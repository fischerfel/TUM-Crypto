SecretKey k = new SecretKeySpec(key.getBytes(), "AES");
Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
cipher.init(Cipher.ENCRYPT_MODE, k);
finalStr = new String(Base64.encode(cipher.doFinal(originalStr.getBytes())));
