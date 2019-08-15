// load the server's public crt (pem), exported from a https website
CertificateFactory cf = CertificateFactory.getInstance("X509");
X509Certificate cert = (X509Certificate)cf.generateCertificate(new
FileInputStream("C:\\certificate.crt"));

// load the pkcs12 key generated with openssl out of the server.crt and server.key (private) (if the private key is stored in the pkcs file, this is not a solution as I need to ship it with my application)
KeyStore ks = KeyStore.getInstance("PKCS12");
ks.load(new FileInputStream("C:\\certificate.pkcs"), "password".toCharArray());
ks.setCertificateEntry("Alias", cert);

TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
tmf.init(ks);

KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, "password".toCharArray());

// create SSLContext to establish the secure connection
SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
