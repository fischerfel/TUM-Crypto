public static void main(String[] args) throws Exception {

KeyStore clientKeyStore = ... // Whatever
KeyStore clientTrustStore = ... // Whatever you need to load

// We build the KeyManager (SSL client credentials we can send)
KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyFactory.init(clientKeyStore, "password".toCharArray());
KeyManager[] km = keyFactory.getKeyManagers();

// We build the TrustManager (Server certificates we trust)
TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustFactory.init(clientTrustStore);
TrustManager[] tm = trustFactory.getTrustManagers();

// We build a SSLContext with both our trust/key managers
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(km, tm, null);
SSLSocketFactory sslSf = sslContext.getSocketFactory();

// We prepare a URLConnection 
URL url = new URL("https://www.google.com");
URLConnection urlConnection = url.openConnection();
// Before actually opening the sockets, we affect the SSLSocketFactory
HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) urlConnection;
httpsUrlConnection.setSSLSocketFactory(sslSf);

// Ready to go !
}
