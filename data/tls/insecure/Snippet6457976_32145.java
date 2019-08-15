PEMReader pemReader = new PEMReader(new StringReader("----- BEGIN ......");
X509Certificate cert = (X509Certificate) pemReader.readObject();

KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(null, null);
ks.setCertificateEntry("some name", cert);

TrustManagerFactory tmf = TrustManagerFactory.getInstance(
    TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ks);

SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, tmf.getTrustManagers(), null);
