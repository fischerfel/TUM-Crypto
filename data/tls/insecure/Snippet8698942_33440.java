//passphrase for keystore
char[] keystorePass="password".toCharArray();

//load own keystore (MyApp just holds reference to application context)
KeyStore keyStore=KeyStore.getInstance("BKS");
keyStore.load(MyApp.getStaticApplicationContext().getResources().openRawResource(R.raw.keystore),keystorePass);

//create a factory
TrustManagerFactory     trustManagerFactory=TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(keyStore);

//get context
SSLContext sslContext=SSLContext.getInstance("TLS");

//init context
sslContext.init(
    null,
    trustManagerFactory.getTrustManagers(), 
    new SecureRandom()
);

//create the socket
Socket socket=sslContext.getSocketFactory().createSocket("hostname",443);
socket.setKeepAlive(true);
