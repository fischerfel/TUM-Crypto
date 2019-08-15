CertificateFactory factory = CertificateFactory.getInstance("X.509");

final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore.load(null, null);    
keyStore.setCertificateEntry("leaf_cert", getCertificate(factory, R.raw.leaf_cert));
keyStore.setCertificateEntry("interm_cert1", getCertificate(factory, R.raw.interm_cert1));
keyStore.setCertificateEntry("interm_cert2", getCertificate(factory, R.raw.interm_cert2));
keyStore.setCertificateEntry("root_cert", getCertificate(factory, R.raw.root_cert));

final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keyStore);

final SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());

return sslContext.getSocketFactory();
