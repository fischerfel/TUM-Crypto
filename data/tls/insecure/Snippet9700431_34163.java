CertificateFactory cf = CertificateFactory.getInstance("X509");
Certificate cer = cf.generateCertificate(new FileInputStream("myFile.cer"));

KeyStore defaultKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
defaultKeyStore.load(null, "".toCharArray());
defaultKeyStore.setCertificateEntry("alias", cer);

trustManagerFactory.init(defaultKeyStore);
keyManagerFactory.init(defaultKeyStore, "".toCharArray());

SSLContext ctx = SSLContext.getInstance("SSL");
ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
