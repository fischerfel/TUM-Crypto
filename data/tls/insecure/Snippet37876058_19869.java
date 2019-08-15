  MyOwnService svc = new MyOwnService(getServerURL(), MYOWNSERVICE_QNAME);
...
...
private URL getServerURL() throws IOException {
  URL url = new URL((String) cfg.get(ConfigData.SERVER_URL));

  HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

  try {
    con.setSSLSocketFactory(getFactory(new File("/etc/pki/wildfly/client.keystore"), "123456"));
  } catch (Exception exc) {
    throw new IOException("Client certificate error!", exc);
  }

  return url;
}

private SSLSocketFactory getFactory(File pKeyFile, String pKeyPassword ) 
  throws ... {

  KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
  KeyStore keyStore = KeyStore.getInstance("PKCS12");

  InputStream keyInput = new FileInputStream(pKeyFile);
  keyStore.load(keyInput, pKeyPassword.toCharArray());
  keyInput.close();

  keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

  SSLContext context = SSLContext.getInstance("TLS");
  context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

  return context.getSocketFactory();
}
