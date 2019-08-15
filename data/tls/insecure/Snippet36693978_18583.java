char[] password = config.getString("https.password").toCharArray();
SSLContext context = SSLContext.getInstance("TLS");
KeyStore ks = KeyStore.getInstance("PKCS12");

ks.load(App.class.getClassLoader().getResourceAsStream("keys/server.p12"), password);
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");

keyManagerFactory.init(ks, password);
context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

HttpsConnectionContext ctx = ConnectionContext.https(context);
