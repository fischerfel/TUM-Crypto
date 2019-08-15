 byte key_bytes[] = "12345678".getBytes();
 SecretKeySpec _keyspec = new SecretKeySpec(key_bytes, "DES");
 Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); // Yes, I know I shouldn't use DES
 cipher.init(Cipher.ENCRYPT_MODE, _keyspec);

 byte[] utf8 = value.getBytes("UTF8");
 byte[] enc = cipher.doFinal(utf8);   // Encrypt

 String encrypted = new String(new Base64().encode(enc));

 return URLEncoder.encode(encrypted, "UTF-8");
