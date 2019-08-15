KeyStore trustStore = KeyStore.getInstance("JKS");
InputStream tsis = new FileInputStream("trustedcerts.jks");
trustStore.load(tsis, "clientpublicpw".toCharArray());
tsis.close();

KeyStore serverKeyStore = KeyStore.getInstance("JKS");
InputStream ksis = new FileInputStream("server.jks");
clientKeyStore.load(ksis.close(), "serverprivatepw".toCharArray());
ksis.close();

TrustManagerFactory tmf = 
    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(trustStore);

KeyManagerFactory kmf = 
    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(serverKeyStore, "serverprivatepw".toCharArray());

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

SSLServerSocketFactory sf = sslContext.getServerSocketFactory();
SSLServerSocket ss = (SSLServerSocket)sf.createServerSocket(SslServer.PORT);
ss.setNeedClientAuth(true);
