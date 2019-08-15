// Loading the certificate in asserts
CertificateFactory cf = CertificateFactory.getInstance("X.509");
AssetManager assManager = context.getAssets();
caInput = assManager.open("certif.crt");
Certificate ca = cf.generateCertificate(caInput);
// Create a KeyStore containing different CAs
String keyStoreType = KeyStore.getDefaultType();
KeyStore keyStore = KeyStore.getInstance(keyStoreType);
keyStore.load(null, null);
keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager to store certificates
String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
tmf.init(keyStore);

// Creates a context for use ssl certificate
SSLContext context = SSLContext.getInstance("TLS");
context.init(null, tmf.getTrustManagers(), null);
