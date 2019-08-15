String privateKey = "someprivatekey";
String data = "dataToEncrypt";

DESKeySpec keySpec = new DESKeySpec(privateKey.getBytes("UTF-8"));
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
SecretKey key = keyFactory.generateSecret(keySpec);

byte[] dataToBytes = data.getBytes("UTF-8");      

Cipher cipher = Cipher.getInstance("DES"); 
cipher.init(Cipher.ENCRYPT_MODE, key);

// send this string to server 
String encryptedStr = Base64.encodeToString(cipher.doFinal(dataToBytes), 0);
