CertificateFactory cf = CertificateFactory.getInstance("X.509");
caInput = new BufferedInputStream(getMyContext().getResources().openRawResource(R.raw.proxyserver));
Certificate ca = cf.generateCertificate(caInput);

String keyStoreType = KeyStore.getDefaultType();
KeyStore keyStore = KeyStore.getInstance(keyStoreType);
keyStore.load(null, null);
keyStore.setCertificateEntry("ca", ca);

String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
tmf.init(keyStore);

SSLContext context = SSLContext.getInstance("TLSv1.2");
context.init(null, tmf.getTrustManagers(), null);

URL url = new URL("https://192.168.1.177:443");
conn = (HttpsURLConnection)url.openConnection();
conn.setSSLSocketFactory(context.getSocketFactory());
conn.setReadTimeout(10000);
conn.setConnectTimeout(15000);
conn.setRequestMethod("POST");
conn.setDoInput(true);
conn.setDoOutput(true);
conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
conn.connect();
