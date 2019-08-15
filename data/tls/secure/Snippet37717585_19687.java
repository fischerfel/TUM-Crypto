final KeyStore localTrustStore = KeyStore.getInstance("BKS"); //NON-NLS
final InputStream in = context.getResources().openRawResource(R.raw.syncapp);
localTrustStore.load(in, "secret".toCharArray()); //Keystore pw
in.close();

final SSLContext sslContext = SSLContext.getInstance("TLSv1.2"); //NON-NLS

final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(localTrustStore);

final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(localTrustStore, "secret".toCharArray()); //privat key pw

sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

serverSocket = sslContext.getServerSocketFactory().createServerSocket(SERVER_PORT);
((SSLServerSocket) serverSocket).setNeedClientAuth(true);
