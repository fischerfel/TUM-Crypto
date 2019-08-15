String passwd = .....";
URL url = new URL("https://.........");

KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
KeyStore keyStore = KeyStore.getInstance("Windows-MY");
keyStore.load(null, passwd.toCharArray());
keyManagerFactory.init(keyStore, passwd.toCharArray());

SSLContext context = SSLContext.getInstance("TLS");
context.init(keyManagerFactory.getKeyManagers(), null, null);
SSLSocketFactory socketFactory = context.getSocketFactory();

HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
conn.setSSLSocketFactory(socketFactory);
