SSLContext sslContext = SSLContext.getInstance("TLS");
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
KeyStore keyStore = KeyStore.getInstance("JKS");
FileInputStream is = new FileInputStream(new File(...));
try {
    keyStore.load(is, "password".toCharArray());
    keyManagerFactory.init(localKeyStore, "password".toCharArray());
} finally {
    is.close();
}
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyStore = KeyStore.getInstance("JKS");
is = new FileInputStream(new File(...));
try {
    keyStore.load(is, "password".toCharArray());
    trustManagerFactory.init(keyStore);
} finally {
    is.close();
}
sslContext.init(keyManagerFactory.getKeyManagers(),
    trustManagerFactory.getTrustManagers(), new SecureRandom());
URL url = ...
URLConnection connection = url.openConnection();
((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
connection.setReadTimeout(40000);
connection.setConnectTimeout(40000);
connection.setDoInput(true);
connection.setDoOutput(false);
int respCode = ((HttpURLConnection) connection).getResponseCode();
if (respCode != 200)
    throw new RuntimeException(...);
BufferedInputStream bufferedIs = new BufferedInputStream(connection.getInputStream());
try {
    byte[] buf = new byte[512];
    int j;
    while ((j = bufferedIs.read(buf)) != -1) {
        ...
} finally {
    bufferedIs.close();
}
