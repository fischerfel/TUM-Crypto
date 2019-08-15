// load the certificate
InputStream fis = this.getClass().getResourceAsStream("/path/to/redlink-CA.crt");
CertificateFactory cf = CertificateFactory.getInstance("X.509");
Certificate cert = cf.generateCertificate(fis);

// load the keystore that includes self-signed cert as a "trusted" entry
KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); 
keyStore.load(null, null);
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
keyStore.setCertificateEntry("CA", cert);
tmf.init(keyStore);
SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(null, tmf.getTrustManagers(), null);

// build the rest client
ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
clientBuilder.sslContext(ctx);
