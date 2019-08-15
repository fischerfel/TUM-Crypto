// this is the encripted text
byte[] PinBytes = Base64.decodeBase64(encryptedText.getBytes("utf-8"));

byte[] VectorBytes = Base64.decodeBase64(vectorKey.getBytes("utf-8"));

byte[] SecretKeyBytes = Base64.decodeBase64(secretKey.getBytes("utf-8")); 

// initialize the vector with the one you receive               
IvParameterSpec spec = new IvParameterSpec(VectorBytes);

// create the key. DESede should be correct, but if it doesn't work try also with DES
Key key = new SecretKeySpec(SecretKeyBytes, "DESede");

// Initialize the cipher
Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");

// decrypt the string
c.init(Cipher.DECRYPT_MODE, key, spec);
byte[] decodedDecryptedBytes = c.doFinal(PinBytes);
