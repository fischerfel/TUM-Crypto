SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
DESKeySpec keySpecEncrypt = new DESKeySpec(ParamsProvider.SERVER_ECRYPTION_SECRETKEY2); //Secret key is a byte[8] = {1, 2, 3, 4, 5, 6, 7, 8}
SecretKey keyEncrypt = keyFactory.generateSecret(keySpecEncrypt);

// Create the cipher 
Cipher desCipher = Cipher.getInstance("DES/CFB8/NoPadding");

// Initialize the cipher for encryption
desCipher.init(Cipher.ENCRYPT_MODE, keyEncrypt);

// Encrypt the text
byte[] textEncrypted = desCipher.doFinal(data.getBytes("UTF-8"));

//B64 encoding and return
byte[] encryptedB64ByteArray = (new org.apache.commons.codec.binary.Base64()).encode(textEncrypted);
return new String(encryptedB64ByteArray, "UTF8");
