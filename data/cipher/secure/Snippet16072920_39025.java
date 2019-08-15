File keyStoreFile = new File("path/to/keystore/file.jks");
InputStream inputStream = new FileInputStream(keyStoreFile);

KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore.load(inputStream, "password".toCharArray());
Key key = keyStore.getKey("yourKeyAlias", "changeit".toCharArray());
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, key);
byte[] encrypted = getEncripted();
byte[] decrypted = cipher.doFinal(encrypted);
