KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
keyStore.load(null);
KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(mAlias, null);
Cipher cip = null;
cip = Cipher.getInstance("RSA/ECB/NoPadding");
cip.init(Cipher.DECRYPT_MODE, entry.getPrivateKey());
byte[] decryptedBytes = cip.doFinal(Base64.decode(encrypted64, Base64.DEFAULT));
String plainText = new String(decryptedBytes);
