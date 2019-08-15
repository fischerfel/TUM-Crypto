// We create the client PKCS12 certificate
final KeyStore clientCertificateKeyStore = KeyStore.getInstance("PKCS12");
try
{
  clientCertificateKeyStore.load(this.clientCertificatePayload, this.clientCertificatePassword.toCharArray());
}
finally
{
  this.clientCertificatePayload.close();
}

final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
keyManagerFactory.init(clientCertificateKeyStore, clientCertificatePassword.toCharArray());

final KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyManagers, null, null);

final URL url = new URL("https://myuri.com");
final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sslContext.getSocketFactory());
httpURLConnection.setReadTimeout(getReadTimeout());
httpURLConnection.setConnectTimeout(getConnectTimeout());
httpURLConnection.setDoInput(true);
httpURLConnection.setRequestMethod("POST");
httpURLConnection.setDoOutput(true);

final OutputStream outputStream = httpURLConnection.getOutputStream();
final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
bufferedWriter.write(body);
bufferedWriter.flush();
bufferedWriter.close();
outputStream.close();

httpURLConnection.connect();
//...
