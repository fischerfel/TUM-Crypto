byte key_bytes[] = "12345678".getBytes();
SecretKeySpec _keyspec = new SecretKeySpec(key_bytes, "DES");
Cipher dcipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
dcipher.init(Cipher.DECRYPT_MODE, _keyspec);

byte[] dec = new Base64().decode(value);
byte[] utf8 = dcipher.doFinal(dec);  // Decrypt, throws exception
return new String(utf8, "UTF8");
