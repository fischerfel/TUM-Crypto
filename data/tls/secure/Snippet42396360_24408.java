char[] ks_passphrase = "passphrase".toCharArray();
char[] ts_passphrase = "changeit".toCharArray();

KeyStore ks = KeyStore.getInstance("JKS");
ks.load(new FileInputStream("testkeys"), ks_passphrase);

KeyStore ts = KeyStore.getInstance("JKS");
ts.load(new FileInputStream("samplecacerts"), ts_passphrase);

KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

kmf.init(ks, ks_passphrase);
tmf.init(ts);

SSLContext ctx = SSLContext.getInstance("TLSv1.2");
ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

SSLSocketFactory factory = ctx.getSocketFactory();

System.out.println("Client - TLS context initialized, creating socket");

SSLSocket socket = (SSLSocket) factory.createSocket(HOST, PORT);
