String privateKey = "someprivatekey";
String data = "dataToEncrypt";

DESKeySpec keySpec = new DESKeySpec(privateKey.getBytes("UTF-8"));
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
SecretKey key = keyFactory.generateSecret(keySpec);

byte[] dataToBytes = data.getBytes("UTF-8");      

Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); 
cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[8]));

// send this string to server 
String encryptedStr = Base64.encodeToString(cipher.doFinal(dataToBytes), 0);
