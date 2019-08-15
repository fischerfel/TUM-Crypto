byte[] keyBytes = Base64.decodeBase64(publicKeyData);
// Get Public Key
X509EncodedKeySpec rsaPublicKeySpec = new X509EncodedKeySpec(keyBytes);
KeyFactory fact = KeyFactory.getInstance("RSA");
PublicKey publicKey = fact.generatePublic(rsaPublicKeySpec);
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);
encryptedData = cipher.doFinal(dataToEncrypt);﻿   
