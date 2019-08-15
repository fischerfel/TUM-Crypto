// Generate a temporary key. In practice, you would save this key
// Encrypting with AES Using a Pass Phrase
 KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes, 100, 128);
 SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
 SecretKey aesKey = keyFactory.generateSecret(keySpec);
ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

// Prepare the parameters to the cipthers
IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());
ecipher.init(Cipher.ENCRYPT_MODE, aesKey, ivParameterSpec);
