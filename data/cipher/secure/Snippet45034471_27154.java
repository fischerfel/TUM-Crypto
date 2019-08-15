String Test = "Lorem ipsum dolor sit amet, ...";
String password = "test";

KeyGenerator kgen = KeyGenerator.getInstance("AES");
SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
sr.setSeed(password.getBytes("UTF8"));
kgen.init(256, sr);
SecretKey skey = kgen.generateKey();

Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");
c.init(Cipher.ENCRYPT_MODE, skeySpec);
byte[] decrypted = c.doFinal(Test.getBytes());
decrypted = Base64.encodeBase64(decrypted);
byte[] iv = Base64.encodeBase64(c.getIV());
Log.d("encryptString", new String(decrypted));
Log.d("encryptString iv", new String(iv));
