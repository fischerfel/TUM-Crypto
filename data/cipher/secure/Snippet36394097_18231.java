KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
ks.load(null);

KeyStore.PrivateKeyEntry keyEntry = 
        (KeyStore.PrivateKeyEntry) ks.getEntry("mykeypair", null);
PrivateKey privKey = keyEntry.getPrivateKey();

Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
c.init(Cipher.ENCRYPT_MODE, privKey);
byte[] cipherText = c.doFinal(clearText, 0, clearText.length);
