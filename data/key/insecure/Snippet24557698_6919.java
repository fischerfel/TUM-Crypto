String my_key =  "2bc7fa12d..." // String of length 128

Security.addProvider(new BouncyCastleProvider());

byte[] original = my_key.getBytes();
key = new SecretKeySpec(original, "AES");

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
byte[] encryptedValue = Base64.encode(encrypted);

return new String(encryptedValue);
