trustStore = KeyStore.getInstance("bks");
trustStore.load(null, null);
caCertificate = getX509Certificate("/some/path/ca.crt");
trustStore.setCertificateEntry("ca-cert", caCertificate);

keyStore = KeyStore.getInstance("pkcs12");
keyStore.load(null, null);
InputStream is = new FileInputStream("/some/path/client.p12");
keyStore.load(is, "passwd".toCharArray());

TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
trustManagerFactory.init(trustStore);
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
keyManagerFactory.init(keyStore, null);

context = SSLContext.getInstance("TLS");
context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

URL url = new URL("https://www.backend.com");
HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
connection.setSSLSocketFactory(context.getSocketFactory());
connection.setDoInput(true);
connection.setDoInput(true);
BufferedReader urlReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
String inputLine;
while ( (inputLine=urlReader.readLine()) != null ){
    System.out.println(inputLine);
}
