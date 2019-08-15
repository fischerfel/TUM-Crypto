KeyGenerator kgen = KeyGenerator.getInstance("AES");
kgen.init(128);
SecretKey skAESPayloadKey = kgen.generateKey();
byte[] raw = skAESPayloadKey.getEncoded();

SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
byte[] baEncSerialNbr = cipher.doFinal(baSerialNbr);
