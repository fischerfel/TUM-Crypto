private void setupAndConnect() {
URL url = new URL("https://host.dom/xyz");
HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(/*keyManagers*/null, /*trustManagers*/null, /*new SecureRandom()*/null);    // simple here

conn.setSSLSocketFactory(new SecureSSLSocketFactory(sslContext.getSocketFactory(), new MyHandshakeCompletedListener()));

// conn.set... /* set other parameters */
conn.connect();
