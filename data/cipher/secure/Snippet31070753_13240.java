c = Cipher.getInstance("RSA/ECB/NoPadding");
c.init(Cipher.ENCRYPT_MODE, pubKeyNew);
encodeFile = c.doFinal(b);
