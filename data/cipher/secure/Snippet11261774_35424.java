privateKey = KeyChain.getPrivateKey(context,mAlias);
byte[] data = // some biary data
Cipher rsasinger = javax.crypto.Cipher.getInstance("RSA/ECB/PKCS1PADDING");
rsasinger.init(Cipher.ENCRYPT_MODE, privkey);

byte[] signed_bytes = rsasinger.doFinal(data);
