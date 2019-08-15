Cipher rsa;
rsa = Cipher.getInstance("RSA");
rsa.init(Cipher.ENCRYPT_MODE, privateKey);
byte[] enc = rsa.doFinal(str.getBytes());
