KeyGenerator kgen = KeyGenerator.getInstance("AES");
kgen.init(128);  // or 192 or 256
SecretKey skey = kgen.generateKey();
byte[] raw = skey.getEncoded();
SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
