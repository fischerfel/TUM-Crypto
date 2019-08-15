// use local trust store (CA)
TrustManagerFactory tmf;
CertificateFactory cf = CertificateFactory.getInstance("X.509");
InputStream srvIn = getAssets().open("messaging.pem");
Certificate ca = cf.generateCertificate(srvIn);
KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore.load(null, null);
keyStore.setCertificateEntry("ca", ca);
tmf = TrustManagerFactory.getInstance("X509");
tmf.init(keyStore);
// load client certificate
KeyStore clientKeyStore = null;
InputStream clIn = getAssets().open("raw_key_file");
clientKeyStore = KeyStore.getInstance("BKS");
clientKeyStore.load(clIn, "mykeystorepassword".toCharArray());
KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
kmf.init(clientKeyStore, "mykeystorepassword".toCharArray());
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
options.setSocketFactory(sslContext.getSocketFactory());
