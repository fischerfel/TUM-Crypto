Cipher c = Cipher.getInstance("AESWrap", "SunJCE");
c.init(Cipher.WRAP_MODE, secretKey);
byte[] result = c.wrap(someKey);
