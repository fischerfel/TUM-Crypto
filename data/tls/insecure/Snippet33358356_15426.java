KeyStore ks = KeyStore.getInstance("PKCS12");

InputStream ins = this.getClass().getClassLoader().getResourceAsStream("certs/tester1024.pfx");
ks.load(ins, "1234".toCharArray());
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SUNX509");
kmf.init(ks, "1234".toCharArray());
SSLContext sc = SSLContext.getInstance("TLS");
sc.init(kmf.getKeyManagers(), null, null);

URL obj = new URL(httpURL);

HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
if (connection instanceof HttpsURLConnection) {
    ((HttpsURLConnection)connection)
    .setSSLSocketFactory(sc.getSocketFactory());
}           
