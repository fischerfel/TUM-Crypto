...

PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
privKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, privKey);
cyphertext= cipher.doFinal(plaintext);
...
caPublicKey = certificate.getPublicKey();
cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.DECRYPT_MODE, caPublicKey);
plaintext= cipher.doFinal(cyphertext);
