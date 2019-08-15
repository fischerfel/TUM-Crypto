KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
