String keyAlg = "AES";
String cipherAlg = "AES/ECB/PKCS5Padding";
byte[] data = new byte[]{14,23,3,45,62};
byte[] pswdBytes = new byte[]{1,2,3,4,6};
MessageDigest digest = MessageDigest.getInstance("MD5");
byte[] hash = digest.digest(pswdBytes);


Key key = new SecretKeySpec(hash , keyAlg);
Cipher cipher = Cipher.getInstance(cipherAlg);
cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] encVal = cipher.doFinal(data);
