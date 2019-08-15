String keyStoreType = KeyStore.getDefaultType();
KeyStore keyStore = KeyStore.getInstance(keyStoreType);
KeyStore.load(context.getResouces().openRawResource(R.raw.mykeystore), "mypass".toCharArray();

String keyalg = KeyManagerFactory.getDefaultAlgorithm();
KeyManagerFactory kmf = KeyManagerFactory.getInstance(keyalg);
kmf.init(keyStore, "mypass".toCharArray());


SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), null, null);
SSLServerSocket serverSocket = (SSLServerSocket)sslContext.getServerSocketFactory().createServerSocket(3333);

SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

//not shown: create BufferedReader from sslSocket.getInputStream(), while-loop for incoming messages
