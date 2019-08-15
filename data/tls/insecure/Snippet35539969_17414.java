private static final String CA_FILE = "ca.pem";
private static final String CERTIFICATE_FILE = "app_01.pfx";
private static final char[] CERTIFICATE_KEY = "password".toCharArray();

[...]

CertificateFactory cf = CertificateFactory.getInstance("X.509");
String algorithm = TrustManagerFactory.getDefaultAlgorithm();

InputStream ca = this.context.getAssets().open(CA_FILE);
KeyStore trustStore = KeyStore.getInstance("PKCS12");
trustStore.load(null);
Certificate caCert = cf.generateCertificate(ca);
trustStore.setCertificateEntry("CA Name", caCert);
CustomTrustManager tm = new CustomTrustManager(trustStore);
ca.close();

InputStream clientCert = this.context.getAssets().open(CERTIFICATE_FILE);
KeyStore keyStore = KeyStore.getInstance("PKCS12");
keyStore.load(clientCert, CERTIFICATE_KEY);
Log.e("KeyStore", "Size: " + keyStore.size());
KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
kmf.init(keyStore, CERTIFICATE_KEY);
clientCert.close();

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), new TrustManager[]{tm}, null);

[...]

((HttpsURLConnection) con).setSSLSocketFactory(sslContext.getSocketFactory());
