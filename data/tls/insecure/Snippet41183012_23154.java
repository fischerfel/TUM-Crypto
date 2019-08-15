KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
kmf.init(keyStore, clientCertPassword.toCharArray());
KeyManager[] keyManagers = kmf.getKeyManagers();
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyManagers, null, null);
