URL url = ...
HttpURLConnection connection = (HttpURLConnection) url.openConnection();
connection.setRequestMethod("POST");
connection.setRequestProperty("Content-type", "application/soap+xml; charset=UTF-8");
connection.setConnectTimeout(300000);
connection.setReadTimeout(300000);
connection.setDoInput(true);
connection.setDoOutput(true);
HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
SSLContext sslContext = SSLContext.getInstance("TLS");
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
FileInputStream is = new FileInputStream(new File(...));
try {
    keyStore.load(is, "password".toCharArray());
    keyManagerFactory.init(keyStore, "password".toCharArray());
} finally {
    is.close();
}
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
is = new FileInputStream(new File(...));
try {
    keyStore.load(is, "password".toCharArray());
    trustManagerFactory.init(keyStore);
} finally {
    is.close();
}
sslContext.init(keyManagerFactory.getKeyManagers(),
    trustManagerFactory.getTrustManagers(),new SecureRandom());
httpsConnection.setSSLSocketFactory(sslContext.getSocketFactory());
httpsConnection.setHostnameVerifier(new HostnameVerifier() {
    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
});
try {
    OutputStream os = connection.getOutputStream();
    try {
        String s = ...;
        os.write(s.getBytes());
    } finally {
        os.close();
    }
    System.out.println("Response code: " + connection.getResponseCode());
} finally {
    connection.disconnect();
}
