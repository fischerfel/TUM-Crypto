DataInputStream in=new DataInputStream(ctx.openFileInput(PRIVKEY_FILE));
byte[] data=new byte[in.available()];
in.readFully(data);

PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(data);
KeyFactory kf = KeyFactory.getInstance("RSA");
privKey = kf.generatePrivate(keySpec);

decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
decryptCipher.init(Cipher.DECRYPT_MODE, privKey);
