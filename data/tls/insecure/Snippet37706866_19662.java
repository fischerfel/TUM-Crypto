private static SSLSocketFactory getFactory() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException 
{
  KeyStore roots = KeyStore.getInstance("Windows-ROOT", new SunMSCAPI());
  KeyStore personal = KeyStore.getInstance("Windows-MY", new SunMSCAPI());

  roots.load(null,null);
  personal.load(null,null);

  TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
  tmf.init(roots);

  KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
  kmf.init(personal, "".toCharArray());

  SSLContext context = SSLContext.getInstance("TLS");

  context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

  return context.getSocketFactory();
}
