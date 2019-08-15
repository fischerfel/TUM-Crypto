MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(secret.getBytes("UTF-8"));
byte[] digest = md.digest();

SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
Cipher cipher = null;
cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, newKey);
byte[] encryptedData = cipher.doFinal(textBytes);

String encryptedDataStr = Base64.encodeToString(encryptedData, Base64.DEFAULT)
