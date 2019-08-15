KeyStore keyStore = KeyStore.getInstance("jks");
keyStore.load(TestFramework.class.getResourceAsStream("/security/keystore.jks"), password.toCharArray());
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
keyManagerFactory.init(keyStore, password.toCharArray());

KeyStore trustStore = KeyStore.getInstance("jks");
trustStore.load(TestFramework.class.getResourceAsStream("/security/truststore"), "ebxmlrr".toCharArray());
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
trustManagerFactory.init(trustStore);

SSLContext context = SSLContext.getInstance("SSL");
context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

...

HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
urlConnection.setSSLSocketFactory(context.getSocketFactory());
