KeyFactory kf1 = KeyFactory.getInstance("RSA");
PublicKey pkPublic1 = kf1.generatePublic(publicKeySpec1);

Cipher pkCipher1 = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
pkCipher1.init(Cipher.DECRYPT_MODE, pkPublic1)
