KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
PrivateKey privateKey = privateKeyEntry.getPrivateKey();
Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
output.init(Cipher.DECRYPT_MODE, privateKey);
