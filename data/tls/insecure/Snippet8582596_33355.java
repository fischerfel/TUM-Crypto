KeyStore ksjks = KeyStore.getInstance(KeyStore.getDefaultType());
ksjks.load(new FileInputStream("/path/to/your/p12/file"),"password".toCharArray());

KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ksjks, "password".toCharArray());

SSLContext sslContext = SSLContext.getInstance("SSLv3");
sslContext.init(kmf.getKeyManagers(), null, null);

SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
