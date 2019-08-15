Cipher encryptCipher = Cipher.getInstance("RSA");
encryptCipher.init(Cipher.ENCRYPT_MODE, Encrypt.getCert());
byte[] message = "Hello World".getBytes("UTF8");
byte[] encrypted = encryptCipher.doFinal(message);
System.out.println(Arrays.toString(encrypted));
