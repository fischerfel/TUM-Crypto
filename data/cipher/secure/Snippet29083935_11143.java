SecretKey key = KeyGenerator.getInstance("AES").generateKey();
String encoded = Base64.getEncoder().encodeToString(key.getEncoded()); 
out.write(encoded);

byte[] raw = Base64.getDecoder().decode(fileIn.nextLine());
key = new SecretKeySpec(raw, "AES");
Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
dcipher.init(Cipher.DECRYPT_MODE, key);
