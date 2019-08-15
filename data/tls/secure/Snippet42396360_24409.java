char[] ks_passphrase = "".toCharArray();
char[] ts_passphrase = "".toCharArray();

KeyStore ks = KeyStore.getInstance("PKCS12");
ks.load(new FileInputStream("client.p12"), ks_passphrase);

KeyStore ts = KeyStore.getInstance("PKCS12");
ts.load(new FileInputStream("client.p12"), ts_passphrase);

KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

kmf.init(ks, ks_passphrase);
tmf.init(ts);

SSLContext ctx = SSLContext.getInstance("TLSv1.2");
ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

SSLSocketFactory factory = ctx.getSocketFactory();

System.out.println("Client - TLS context initialized, creating socket");

SSLSocket socket = (SSLSocket) factory.createSocket(HOST, PORT);
