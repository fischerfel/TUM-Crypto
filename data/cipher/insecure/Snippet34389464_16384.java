 KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
 SecretKey myDesKey = keygenerator.generateKey();
 Cipher desCipher;
 desCipher = Cipher.getInstance("DES");
 byte[] text = "Hello".getBytes("UTF8");


 desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
 byte[] textEncrypted = desCipher.doFinal(text);
 String s = new String(textEncrypted);
 System.out.println(s);
