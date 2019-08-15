AssetManager assetManager = getAssets();
InputStream keyStoreInputStream = assetManager.open("keystore.bks");

KeyStore keyStore = KeyStore.getInstance("BKS");
keyStore.load(null,null);
keyStore.load(keyStoreInputStream, KEY_PASSWORD);

TrustManagerFactory trustManagerFactory =
    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(keyStore);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

SSLSocketFactory sslsocketfactory = sslContext.getSocketFactory();
SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("192.168.1.212", 4444);

InputStream inputStream = assetManager.open(MainActivity.FILE_NAME);

OutputStream outputStream = sslsocket.getOutputStream();

int count;
byte[] buffer = new byte[12 * 1024];
while ((count = inputStream.read(buffer)) > 0) {
    outputStream.write(buffer, 0, count);
}
