KeyStore primaryKeyStore = getKeyStore(keyStoreFile, password, keyType, provider);
java.security.cert.Certificate certs = primaryKeyStore.getCertificate(aliasName);
cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, certs);
