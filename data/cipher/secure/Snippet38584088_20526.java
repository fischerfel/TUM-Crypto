Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
cipher.init(Cipher.ENCRYPT_MODE, clientPublickey);
scrambled = cipher.doFinal(buffer);
