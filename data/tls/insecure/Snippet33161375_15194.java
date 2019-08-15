// keystore file
File keyFile = new File("C:/mycert.pfx");
KeyStore keyStore = KeyStore.getInstance("PKCS12");
InputStream keyInput = new FileInputStream(keyFile);
keyStore.load(keyInput, pKeyPassword.toCharArray());
keyInput.close();
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());
KeyManager[] km = keyManagerFactory.getKeyManagers();
SSLContext context = SSLContext.getInstance("TLS");
context.init(km, null, new java.security.SecureRandom());
SSLSocketFactory sslFactory = context.getSocketFactory();
conn.setSSLSocketFactory(sslFactory);
