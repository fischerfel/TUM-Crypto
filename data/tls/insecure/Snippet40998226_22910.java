CertificateFactory cf = CertificateFactory.getInstance("X509");
X509Certificate cert = (X509Certificate) cf.generateCertificate(
    this.getClass().getResourceAsStream("/"+"lib/ca.crt")
);

InputStream is=this.getClass().getResourceAsStream("/"+"lib/plainclient.jks");
KeyStore clientKeys = KeyStore.getInstance("JKS");
clientKeys.load(is,"xuanthinh".toCharArray());
KeyManagerFactory clientKeyManager = KeyManagerFactory.getInstance("SunX509");
clientKeyManager.init(clientKeys,"xuanthinh".toCharArray());

KeyStore ks = KeyStore.getInstance("JKS");
is=this.getClass().getResourceAsStream("/"+"lib/serverpub.jks");
ks.load(is,"xuanthinh".toCharArray());
TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
trustManager.init(ks);
//use keys to create SSLSoket
ssl = SSLContext.getInstance("TLS");
ssl.init(
    clientKeyManager.getKeyManagers(), trustManager.getTrustManagers(), 
    SecureRandom.getInstance("SHA1PRNG")
);
