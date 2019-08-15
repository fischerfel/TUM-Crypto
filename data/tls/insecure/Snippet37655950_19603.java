CertificateFactory cf = null;
cf = CertificateFactory.getInstance("X.509");
Certificate ca;
TypedValue returnedValue = new TypedValue();
InputStream cert = context.getResources().openRawResource(R.raw.thecertificate);
ca = cf.generateCertificate(cert);

// Creating a KeyStore containing our trusted CAs
String keyStoreType = KeyStore.getDefaultType();
KeyStore keyStore  = KeyStore.getInstance(keyStoreType);
keyStore.load(null, null);
keyStore.setCertificateEntry("ca", ca);

// Creating a TrustManager that trusts the CAs in our KeyStore.
String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
tmf.init(keyStore);

// Creating an SSLSocketFactory that uses our TrustManager
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, tmf.getTrustManagers(), null);
