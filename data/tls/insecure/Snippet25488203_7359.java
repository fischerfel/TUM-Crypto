KeyStore ks = KeyStore.getInstance("JKS");
ks.load(new FileInputStream(keyStoreFile), "password".toCharArray());
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, "privatepassword".toCharArray());
SSLContext sslCtx = SSLContext.getInstance("TLS");
sslCtx.init(kmf.getKeyManagers(), null, null);
