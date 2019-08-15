PublicKey publicKey=null;
Cipher publicKeyCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
publicKeyCipher.init(Cipher.ENCRYPT_MODE, publicKey);
