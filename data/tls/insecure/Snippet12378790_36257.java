CertificateFactory cf = CertificateFactory.getInstance("X.509");
Certificate certificate = cf.generateCertificate(new FileInputStream(...));

KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore.load(null, "test".toCharArray());
keyStore.setCertificateEntry("admin", certificate);

// Code omitted which repeats the above to set the judge certificate

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keyStore);
SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(null, tmf.getTrustManagers(), null);

factory = ctx.getSocketFactory(); // Or #getServerSocketFactory() if admin and not judge
