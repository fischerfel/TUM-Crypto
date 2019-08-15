SecretKeySpec skeySpec = new SecretKeySpec(pad16(pass), "AES");
Cipher c = Cipher.getInstance("AES");
c.init(Cipher.ENCRYPT_MODE, skeySpec);
byte[] out = c.doFinal( input )
