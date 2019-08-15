DataInputStream in=new DataInputStream(ctx.openFileInput(PUBKEY_FILE));
byte[] data=new byte[in.available()];
in.readFully(data);

PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(data);
KeyFactory kf = KeyFactory.getInstance("RSA");
pubKey = kf.generatePublic(keySpec); --> here the exception is thrown

encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
encryptCipher.init(Cipher.ENCRYPT_MODE, pubKey);             
