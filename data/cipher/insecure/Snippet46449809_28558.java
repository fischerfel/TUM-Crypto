Key key = generateKeyAES();
Cipher c = Cipher.getInstance("AES");
c.init(Cipher.ENCRYPT_MODE, key);
byte[] encVal = c.doFinal(data.getBytes());
return new String(java.util.Base64.getEncoder().encode(encVal));
