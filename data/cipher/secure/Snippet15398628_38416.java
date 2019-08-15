byte [] pkArray=publicKey.getBytes();
X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pkArray);
KeyFactory keyFactory = KeyFactory.getInstance("RSA");
PublicKey pKey =keyFactory.generatePublic(pubKeySpec); //get the exception here
Cipher publicKeyCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
publicKeyCipher.init(Cipher.ENCRYPT_MODE, publicKey);
