Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
c.init(Cipher.ENCRYPT_MODE, k, iv);
byte[] someData = c.update(textData);
byte[] moreData = c.doFinal();

System.out.println("E: " + (someData.length + moreData.length));

c = Cipher.getInstance("AES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, k, iv);
byte[] someDecrypted = c.update(someData);
byte[] moreDecrypted = c.doFinal(moreData);

System.out.println("R: " + (someDecrypted.length + moreDecrypted.length));
