CertificateFactory cf = CertificateFactory.getInstance("X.509");
InputStream keyStoreFile = getAssets().open("raw_key_file");
//keystore trusted
KeyStore keystoreTrust = KeyStore.getInstance("BKS");//Bouncy Castle format for Android
keystoreTrust.load(keyStoreFile, "mykeystorepassword".toCharArray());
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(keystoreTrust);
SLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
options.setSocketFactory(sslContext.getSocketFactory());
