MyAESKey myAESKey = new MyAESKey(new byte[16]);
Cipher aes = Cipher.getInstance("AES");
aes.init(Cipher.ENCRYPT_MODE, myAESKey);
aes.doFinal("owlstead".getBytes());
myAESKey.destroy();
aes.doFinal("owlstead".getBytes());
aes.init(Cipher.ENCRYPT_MODE, myAESKey);
aes.doFinal("owlstead".getBytes());
