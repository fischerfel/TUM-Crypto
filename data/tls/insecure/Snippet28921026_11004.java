KeyStore keyStore = KeyStore.getInstance("BKS");
InputStream is = getResources().openRawResource(R.raw.client);
keyStore.load(is, "yourKeyStorePassword".toCharArray());
is.close();

KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
keyManagerFactory.init(keyStore, "yourKeyStorePassword".toCharArray());

SSLContext sslContext = SSLContext.getInstance("TLS"); 
sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, null);
