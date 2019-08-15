Cipher c = Cipher.getInstance("AES/CTR/NoPadding");
KeyGenerator kg = KeyGenerator.getInstance("AES");
c.init(Cipher.ENCRYPT_MODE, kg.generateKey());
System.out.println(c.update(new byte[1]).length);  // output: 1
System.out.println(c.update(new byte[20]).length); // output: 20
