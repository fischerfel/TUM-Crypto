public static String generatePswdBasedKey(String password){
String finalKey = null;
SecretKey sk = null;
KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, IT, KEY_LENGTH);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
sk = new SecretKeySpec(keyBytes, "AES");
Cipher cipher = Cipher.getInstance(Cifrador.AES_MODE);//AES_MODE = AES/CBC/PKCS5Padding
IvParameterSpec ivParams = new IvParameterSpec(iv);//IV already initialized
cipher.init(Cipher.ENCRYPT_MODE, sk, ivParams);
byte pwdbytes[] = password.getBytes();//I also tried using Base64 to decode... without success
byte cc[] = cipher.doFinal(pwdbytes);
finalKey = Base64.encodeToString(cc, false);  //.encodeToString(byte[] sArr, boolean lineSep)
return finalKey;
