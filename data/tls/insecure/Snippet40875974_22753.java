// Load Certificate
CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
Certificate certificate = certificateFactory.generateCertificate(new FileInputStream(new File("CERTIFICATE_LOCATION")));

// Create TrustStore
KeyStore trustStoreContainingTheCertificate = KeyStore.getInstance("JKS");
trustStoreContainingTheCertificate.load(null, null);

trustStoreContainingTheCertificate.setCertificateEntry("ANY_CERTIFICATE_ALIAS", certificate);

// Create SSLContext
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(trustStoreContainingTheCertificate);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
System.out.println(sslSocketFactory);
