InputStream is = new FileInputStream("cert.crt");

CertificateFactory cf = CertificateFactory.getInstance("X.509");
X509Certificate caCert = (X509Certificate)cf.generateCertificate(is);

TrustManagerFactory tmf = TrustManagerFactory
    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(null); // You don't need the KeyStore instance to come from a file.
ks.setCertificateEntry("caCert", caCert);

tmf.init(ks);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, tmf.getTrustManagers(), null);
