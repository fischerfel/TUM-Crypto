RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(mod,exp);
KeyFactory keyFactory = KeyFactory.getInstance("RSA","BS");
PublicKey publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BS");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
byte[] encryptedBytes = cipher.doFinal(plaintText.getBytes());
byte[] encodedBytes = org.bouncycastle.util.encoders.Base64.encode(encryptedBytes);
String encryptedData = new String(encodedBytes);
