// Step 1: Initialize a ssl context with highest version
ssl_ctx = SSLContext.getInstance("TLSv1.2");

// Step 2: Add certificates to context

// Step 2.1 get private key
int pkeyId = context.getResources().getIdentifier("raw/clientkeypkcs", null, context.getPackageName());
InputStream fis = context.getResources().openRawResource(pkeyId);
DataInputStream dis = new DataInputStream(fis);
byte[] bytes = new byte[dis.available()];
dis.readFully(bytes);
ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
byte[] key = new byte[bais.available()];
KeyFactory kf = KeyFactory.getInstance("RSA");
bais.read(key, 0, bais.available());
bais.close();           
PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec ( key );
PrivateKey ff = kf.generatePrivate (keysp);

//Step 2.2 get certificates
int caresId = context.getResources().getIdentifier("raw/ca_cert", null, context.getPackageName());            
InputStream caCertIS = context.getResources().openRawResource(caresId);
CertificateFactory cacf = CertificateFactory.getInstance("X.509");
X509Certificate caCert = (X509Certificate)cacf.generateCertificate(caCertIS);
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(null); // You don't need the KeyStore instance to come from a file.
ks.setCertificateEntry("caCert", caCert);
tmf.init(ks);

int clientresId = context.getResources().getIdentifier("raw/client_cert", null, context.getPackageName());            
InputStream clientCertIS = context.getResources().openRawResource(clientresId);
CertificateFactory clientcf = CertificateFactory.getInstance("X.509");
X509Certificate clientCert = (X509Certificate)clientcf.generateCertificate(clientCertIS);
KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
ks.setCertificateEntry("clientCert", clientCert);
kmf.init(ks, "***********".toCharArray());
Certificate[] chain = new Certificate[] { clientCert};
//ks.load(null); // You don't need the KeyStore instance to come from a file.
ks.setKeyEntry("importkey", ff, "***********".toCharArray(), chain );           

ssl_ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
