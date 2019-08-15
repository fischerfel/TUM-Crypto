String keyStr = "secret";
byte[] key = (keyStr).getBytes("UTF-8");
MessageDigest sha = MessageDigest.getInstance("MD5");
key = sha.digest(key);
key = Arrays.copyOf(key, 16);
SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
String text = "Some text"

Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
byte[] encrypted = cipher.doFinal(text.getBytes());
