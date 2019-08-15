KeyStore ks = KeyStore.getInstance("PKCS12");
ks.load(new FileInputStream(certificatePath), password.toCharArray());

KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
kmf.init(ks, password.toCharArray());

SSLContext context = SSLContext.getInstance("TLS");
context.init(kmf.getKeyManagers(), null, null);

SSLServerSocketFactory ssf = context.getServerSocketFactory();
serverSocket = (SSLServerSocket) ssf.createServerSocket(2195);
