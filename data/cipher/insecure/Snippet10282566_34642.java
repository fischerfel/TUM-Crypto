final Key key = new SecretKeySpec(seed.getBytes(), "DES");
final Cipher c = Cipher.getInstance("DES");
c.init(Cipher.ENCRYPT_MODE, key);
final byte[] encVal = c.doFinal(s.getBytes());
return new BASE64Encoder().encode(encVal);
