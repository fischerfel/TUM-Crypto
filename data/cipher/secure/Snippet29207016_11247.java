$td = mcrypt_module_open('rijndael-128', '', 'cbc', $iv_array_string);

String password = "mypass";
String encoding = "UTF-8";
String cleanString = "text to encode";

byte[] salt_array = {(byte) 0x98, (byte) 0x71, (byte) 0x1F, (byte) 0x71, (byte) 0x5D, (byte) 0x71, (byte) 0x28, (byte) 0x8F};

//Key
KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt_array, 16, 128);
SecretKey tmp = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(keySpec);
SecretKey key = new SecretKeySpec(tmp.getEncoded(), "AES");

//ciphers
byte[] iv_array = {(byte) 0x98, (byte) 0x71, (byte) 0xF3, (byte) 0x52, (byte) 0x1A, (byte) 0x71, (byte) 0x38, (byte) 0x1F, (byte) 0x75, (byte) 0x1F, (byte) 0x1F, (byte) 0xE0, (byte) 0xEF, (byte) 0x39, (byte) 0x98, (byte) 0x1F};
Cipher encChiper = Cipher.getInstance("AES/CBC/PKCS5Padding");
AlgorithmParameterSpec params = new iv_arrayParameterSpec(iv_array);
encChiper.init(Cipher.ENCRYPT_MODE, key, params);
byte[] crypted = encChiper.doFinal(cleanString.getBytes(encoding));
//output encoded
String base64Crypted = new String(Base64.encodeBase64(crypted), encoding);
