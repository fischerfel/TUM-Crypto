KeyStore keyStore = KeyStore.getInstance("MyKeyStore", "MyProvider");
KeyManagerFactory kmfactory = KeyManagerFactory.getInstance("MyKeyManagerFactory", "MyProvider");
kmfactory.init(keyStore, keystorePinValue.toCharArray());
KeyManager[] kms =  kmfactory.getKeyManagers();
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init( kms, !trustmanagers.isEmpty() ? trustmanagers.toArray(new TrustManager[trustmanagers.size()]) : null, new SecureRandom());
URL url = new URL(urlStr);
HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
InputStream in = urlConnection.getInputStream();
