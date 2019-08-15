KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
                              KeyManagerFactory.getDefaultAlgorithm());
kmfactory.init(trustStore, "password".toCharArray());
KeyManager[] keymanagers =  kmfactory.getKeyManagers();

TrustManagerFactory tmf=TrustManagerFactory
  .getInstance(TrustManagerFactory.getDefaultAlgorithm());

tmf.init(trustStore);

SSLContext sslContext=SSLContext.getInstance("TLSv1.2");

sslContext.init(keymanagers, tmf.getTrustManagers(), new SecureRandom());

SSLSocketFactory factory=sslContext.getSocketFactory();
