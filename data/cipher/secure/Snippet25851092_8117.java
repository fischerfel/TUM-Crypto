RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(mod,exp);
KeyFactory keyFactory = KeyFactory.getInstance("RSA","BS");
PublicKey publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BS");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));//
byte[] coded = Base64.encodeBase64(encryptedBytes);  //used library encode decode
String encryptedData = new String(encodedBytes);
