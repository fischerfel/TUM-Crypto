String type = KeyStore.getDefaultType();
KeyStore store=KeyStore.getInstance(type);
inputStream=this.getClass().getResourceAsStream("test.bks");
store.load(inputStream, password);
inputStream.close();
String keyalg=KeyManagerFactory.getDefaultAlgorithm();
KeyManagerFactory factory=KeyManagerFactory.getInstance(keyalg);
factory.init(store,password);
KeyManager []keyManagers=factory.getKeyManagers();
context=SSLContext.getInstance("SSL");
context.init(keyManagers, null, null);
ServerSocketFactory factory= context.getServerSocketFactory();
serverSocket=(SSLServerSocket)factory.createServerSocket(12345);
while (true) {
    Socket socket = serverSocket.accept();
    new ReceiveSocket(socket).start();
}
