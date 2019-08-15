static String CA_FILE = "ca-cert.pem";
...

FileInputStream fis = new FileInputStream(CA_FILE);
X509Certificate ca = (X509Certificate) CertificateFactory.getInstance("X.509")
                        .generateCertificate(new BufferedInputStream(fis));

KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(null, null);
ks.setCertificateEntry(Integer.toString(1), ca);

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ks);

SSLContext context = SSLContext.getInstance("TLS");
context.init(null, tmf.getTrustManagers(), null);
...
