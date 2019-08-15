Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
Cipher rsa = Cipher.getInstance("RSA/None/NoPadding");
rsa.init(Cipher.ENCRYPT_MODE, publicKey);
byte[] cryptRaw = rsa.doFinal(saltedPassword.getBytes());
