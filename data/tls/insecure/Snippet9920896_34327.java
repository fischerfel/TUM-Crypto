TrustManagerFactory tmf = TrustManagerFactory.getInstance(
    TrustManagerFactory.getDefaultAlgorithm());
KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(null, null); // initialise!
ks.setKeyEntry("dbentry", Base64.decode(sslTrust, Base64.NO_WRAP), null);
tmf.init(ks);
tms = tmf.getTrustManagers();
ss.stm = new SnoopyTrustManager((X509TrustManager) tms[0]);
// ...
SLContext context = SSLContext.getInstance("SSL");
context.init(null, new TrustManager[] { ss.stm } , null);
ss.factory = context.getSocketFactory();
// ...
SocketFactory factory = ss.getFactory();
mSocket = factory.createSocket(host, port);
