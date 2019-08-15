CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");

AssetManager assManager = ShoppingHelperApp.getContext().getAssets();
InputStream is = assManager.open("keystore2.crt");
InputStream caInput = new BufferedInputStream(is);
Certificate ca = cf.generateCertificate(caInput);
caInput.close();

String keyStoreType = KeyStore.getDefaultType();
KeyStore keyStore = KeyStore.getInstance(keyStoreType);
keyStore.load(null, null);
keyStore.setCertificateEntry("ca", ca);

String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
tmf.init(keyStore);

context = SSLContext.getInstance("TLS");
context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
conn.setSSLSocketFactory(context.getSocketFactory());
