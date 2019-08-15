URL url = new URL("https://myurl");
KeyStore ks = KeyStore.getInstance("PKCS12");
FileInputStream fis = new FileInputStream("certificate/cert1.p12");
ks.load(fis, "mypass".toCharArray());
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ks);

KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, "mypass".toCharArray());

SSLContext sslCtx = SSLContext.getInstance("TLS");
sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
SSLSocketFactory sslSF = sslCtx.getSocketFactory();

URLConnection conn = url.openConnection();
if(conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setSSLSocketFactory(sslSF);
}
conn.connect();
