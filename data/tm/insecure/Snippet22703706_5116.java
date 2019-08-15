String domainName ="localhost";

KeyPairGenerator keyPairGenerator;
Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider ());

keyPairGenerator = KeyPairGenerator.getInstance("RSA");

keyPairGenerator.initialize(1024);
KeyPair KPair = keyPairGenerator.generateKeyPair();

X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator(); 


v3CertGen.setSerialNumber(BigInteger.valueOf(Math.abs(new SecureRandom().nextInt())));
v3CertGen.setIssuerDN(new X509Principal("CN=" + domainName + ", OU=None, O=None L=None, C=None"));
v3CertGen.setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30));
v3CertGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365*10)));
v3CertGen.setSubjectDN(new X509Principal("CN=" + domainName + ", OU=None, O=None L=None, C=None"));

v3CertGen.setPublicKey(KPair.getPublic());
v3CertGen.setSignatureAlgorithm("MD5WithRSAEncryption");

X509Certificate pkCertificate = v3CertGen.generateX509Certificate(KPair.getPrivate());  

KeyStore keystore = KeyStore.getInstance("JKS");
keystore.load(null, null);
Random rand = new Random();
String x = rand.nextInt(1000000000) + "";
char[] passCert = x.toCharArray();
keystore.setKeyEntry("user", KPair.getPrivate(), passCert, new X509Certificate[] {pkCertificate});
FileOutputStream fos;

fos = new FileOutputStream("cert.cert");
fos.write(pkCertificate.getEncoded());
fos.close();
KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
Random rand2 = new Random();
String y = rand2.nextInt(1000000000) + "";
char[] password = y.toCharArray();
ks.load(null, password);
PrivateKeyEntry entry = new PrivateKeyEntry(KPair.getPrivate(),
      new java.security.cert.Certificate[]{pkCertificate});
ks.setEntry("newuser",entry , new KeyStore.PasswordProtection(password));

fos = new FileOutputStream("mySrvKeystore3");
ks.store(fos, password);
fos.close();
System.setProperty("javax.net.ssl.keyStore", "mySrvKeystore3");
System.setProperty("javax.net.ssl.keyStorePassword", y);
System.setProperty("javax.net.ssl.trustStore", "mySrvKeystore");
System.setProperty("javax.net.ssl.trustStorePassword", "harinder99");
/////
clientSocket = null;
ServerSocket serverSocket = null;
SSLServerSocket server1 = (SSLServerSocket)null;
String port = JOptionPane.showInputDialog("Enter Port: ", "1234");
try {
    //SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
    TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
           public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
           }

           public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
           }

           public java.security.cert.X509Certificate[] getAcceptedIssuers() {
               return null;
           }
       }
   };
   KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
   kmf.init(ks, y.toCharArray());
   SSLContext ctx = SSLContext.getInstance("TLSv1.2");
   ctx.init(kmf.getKeyManagers(), trustAllCerts, new SecureRandom());
   SSLServerSocketFactory sslserversocketfactory = ctx.getServerSocketFactory();
   server1 = (SSLServerSocket) sslserversocketfactory.createServerSocket(Integer.parseInt(port));
