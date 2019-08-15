String hexCipherText = "539b333b39706d149028cfe1d9d4a407";
String hexSecretKey =  "80000000000000000000000000000000" +
                       "00000000000000000000000000000001";

byte[] secretKey = DatatypeConverter.parseHexBinary (hexSecretKey);
byte[] cipherText = DatatypeConverter.parseHexBinary (hexCipherText);

Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey,"AES");
cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

byte[] plainText = cipher.doFinal(cipherText);
String hexPlainText = DatatypeConverter.printHexBinary(plainText);
