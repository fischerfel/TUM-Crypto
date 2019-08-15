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
