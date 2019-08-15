TrustManagerFactory tmf = TrustManagerFactory
    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
KeyStore ks = KeyStore.getInstance("JKS");
FileInputStream fis = new FileInputStream("/.../example.jks");
ks.load(fis, null);
// or ks.load(fis, "thepassword".toCharArray());
fis.close();

tmf.init(ks);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, tmf.getTrustManagers(), null);

URL url = new URL("https://somewebsite.com");
HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
conn.setSSLSocketFactory(sslContext.getSocketFactory());

InputStream is = conn.getInputStream();
