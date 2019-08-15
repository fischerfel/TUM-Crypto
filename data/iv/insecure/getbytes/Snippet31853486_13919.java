byte [] input ;
byte [] keyBytes = "12345678".getBytes();
byte [] ivBytes ="input123".getBytes();

SecretKeySpec key = new SecretKeySpec(keyBytes,"DES");
IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
Cipher cipher;
byte[] cipherText;
int ctLength;
