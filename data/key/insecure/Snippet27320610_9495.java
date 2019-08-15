String metaKey = "ourSecretKey";
String encodedKey = "this is supposed to be a secret";
byte[] encodedKeyBytes = new byte[(int)encodedKey.length()];
encodedKeyBytes = encodedKey.getBytes("UTF-8");
KeyStoreParameter ksp = null;

//String algorithm = "DES";
String algorithm = "DESede";
SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
SecretKeySpec secretKeySpec = new SecretKeySpec(encodedKeyBytes, algorithm);
SecretKey secretKey = secretKeyFactory.generateSecret(secretKeySpec);

KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

keyStore.load(null);

KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
keyStore.setEntry(metaKey, secretKeyEntry, ksp);

keyStore.store(null);

String recoveredSecret = "";
if (keyStore.containsAlias(metaKey)) {
    KeyStore.SecretKeyEntry recoveredEntry = (KeyStore.SecretKeyEntry)keyStore.getEntry(metaKey, ksp);
    byte[] bytes = recoveredEntry.getSecretKey().getEncoded();
    for (byte b : bytes) {
        recoveredSecret += (char)b;
     }
}
Log.v(TAG, "recovered " + recoveredSecret);
