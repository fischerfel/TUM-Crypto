InputStream stream = context.getResources().openRawResource(R.raw.keystore);
// BKS seems to be the default but we want to be explicit
KeyStore ks = KeyStore.getInstance("BKS");
ks.load(stream, "www.onlinescoutmanager.co.uk".toCharArray());
stream.close();

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ks);
X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
SSLContext context2 = SSLContext.getInstance("TLS");
context2.init(null, new TrustManager[] { defaultTrustManager }, null);
sslSocketFactory = context2.getSocketFactory();
