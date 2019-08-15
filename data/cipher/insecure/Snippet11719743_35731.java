KeyGenerator kgen = KeyGenerator.getInstance("Blowfish");
SecretKey skey = kgen.generateKey();
byte[] raw = skey.getEncoded();
SecretKeySpec skeySpec = new SecretKeySpec(raw, "Blowfish");

Cipher cipher = Cipher.getInstance("Blowfish");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
String inputString = "This is just an example";
byte[] encrypted = cipher.doFinal(inputString.getBytes());

Cipher decCipher = Cipher.getInstance("Blowfish");
decCipher.init(Cipher.DECRYPT_MODE, skeySpec);
byte[] decrypted = decCipher.doFinal(encrypted);

assertEquals(inputString, new String(decrypted));
