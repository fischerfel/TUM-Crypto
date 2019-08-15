KeyStore jks = KeyStore.getInstance(KeyStore.getDefaultType());
jks.load(null, null);

KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
jks.setCertificateEntry("alias", cert1); //X509Certificate obtained from windows keystore
kmf.init(jks, new char[0]);

SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(kmf.getKeyManagers(), new TrustManager[]{tm}, null);

sslsocketfactory = sslContext.getSocketFactory();
System.setProperty("https.proxyHost", proxyurl);
System.setProperty("https.proxyPort", proxyport);

Authenticator.setDefault(new MyAuthenticator("proxyID", "proxyPassword"));
URL url = new URL(null, urlStr, new sun.net.www.protocol.https.Handler());
HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
uc.setSSLSocketFactory(sslsocketfactory);
uc.setAllowUserInteraction(true);
uc.setRequestMethod("POST");
uc.connect();
