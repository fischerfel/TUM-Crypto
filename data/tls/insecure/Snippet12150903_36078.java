KeyStore store = KeyStore.getInstance("BKS");
InputStream truststore = mainActivity.getResources().openRawResource(R.raw.trust);
store.load(truststore, "PASSWORD".toCharArray());
TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
tmf.init(store);
SSLContext context = SSLContext.getInstance("TLS");
context.init(null, tmf.getTrustManagers(), new SecureRandom());
Socket socket = context.getSocketFactory().createSocket(ip, port);
