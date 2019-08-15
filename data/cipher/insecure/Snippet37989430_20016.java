Cipher cp = Cipher.getInstance("DESede/ECB/PKCS5Padding");
cp.init(Cipher.ENCRYPT_MODE, key);
byte[] criptati = cp.doFinal(input);
out = new String(Base64.getEncoder().encode(criptati));
