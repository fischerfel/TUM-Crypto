//Preparing keystore with Private Key
BouncyCastleProvider provider = new BouncyCastleProvider();
Security.addProvider(provider);
KeyStore ks = KeyStore.getInstance("pkcs12", provider.getName());
ks.load(new FileInputStream(Constantes.CERT), Constantes.PASS_CERT.toCharArray());
String alias = (String)ks.aliases().nextElement();
PrivateKey pk = (PrivateKey) ks.getKey(alias, Constantes.PASS_CERT.toCharArray());
Certificate[] chain = ks.getCertificateChain(alias);

//Adding Server public key to the keystore ks
FileInputStream is = new FileInputStream(new File(Constantes.SERVER_CERT_CER));
CertificateFactory cf = CertificateFactory.getInstance("X.509");
java.security.cert.X509Certificate cert = (X509Certificate) cf.generateCertificate(is); 
ks.setCertificateEntry("alias", cert);

//Preparing SSL Context
KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, "pass".toCharArray());

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ks);

SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(kmf.getKeyManagers(),tmf.getTrustManagers(), new SecureRandom());
SSLSocketFactory sf = ctx.getSocketFactory();

URL url = new URL("https://...");
HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
conn.setSSLSocketFactory(sf);
conn.setConnectTimeout(0);
conn.connect(); 

//Until here the connection is OK, "Certificate Verify" say whireshark

//Preparing TSA Client and Sign
ExternalDigest digest = new BouncyCastleDigest();
TSAClient tsaClient = new TSAClientBouncyCastle("https://...");

ExternalSignature es = new PrivateKeySignature(pk,"SHA-1","BC");
MakeSignature.signDetached(appearance, digest, es, chain, null, null, tsaClient, 0, CryptoStandard.CMS);
