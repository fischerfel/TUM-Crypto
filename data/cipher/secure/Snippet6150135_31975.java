KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
kpg.initialize(2048);
KeyPair kp = kpg.genKeyPair();

Cipher enc = Cipher.getInstance("RSA");
enc.init(Cipher.ENCRYPT_MODE, kp.getPublic());
String CipherText = new String(enc.doFinal(PlainText.getBytes()));
System.out.println("CipherText: ") + CipherText);

Cipher dec = Cipher.getInstance("RSA");
dec.init(Cipher.DECRYPT_MODE, kp.getPrivate());
PlainText = new String(dec.doFinal(CipherText.getBytes()));
System.out.println("PlainText: " + PlainText);
