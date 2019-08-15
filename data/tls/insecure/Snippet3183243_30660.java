KeyStore ks = ...
/* load the keystore */

KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, password);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), null, null);

URL url = new URL("https://example/");
HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

connection.setSSLSocketFactory(sslContext.getSSLSocketFactory());
