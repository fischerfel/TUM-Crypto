SSLContext ctx = SSLContext.getInstance("SSL");
// Accept-all trust manager
TrustManager[] trustEverything = { new DefaultTrustManager() };       

// Keystore file in local directory
KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(new java.io.FileInputStream("keystore"),"123456".toCharArray());

// Key manager  
KeyManager[] managers;
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, "123456".toCharArray());
managers = kmf.getKeyManagers();

ctx.init(managers, trustEverything, new SecureRandom());
SSLSocketFactory sslFact = (SSLSocketFactory) ctx.getSocketFactory();
// Connect to internal SSL-enabled server
SSLSocket socket = (SSLSocket) sslFact.createSocket("10.131.149.36", 8443);
