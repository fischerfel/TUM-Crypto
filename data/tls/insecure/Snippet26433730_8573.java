String certificateName = "keystore";
String path = "C:\\Users\\Black\\Desktop\\" + certificateName + ".jks";
char[] passphrase = "123456".toCharArray();

KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore.load(new FileInputStream(path), passphrase);
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(keyStore, passphrase);

SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(kmf.getKeyManagers(), null, null);

SSLServerSocketFactory sslserversocketfactory = ctx.getServerSocketFactory();
serverSocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(port);
serverSocket.setEnabledProtocols(new String[] { "TLSv1", "TLSv1.1","TLSv1.2", "SSLv3" });

clientSocket = (SSLSocket) serverSocket.accept();
clientSocket.addHandshakeCompletedListener(new HandshakeCompletedListener() {
            @Override
            public void handshakeCompleted(
                    HandshakeCompletedEvent arg0) {
                System.out.println(arg0.toString());
            }
        });

out = clientSocket.getOutputStream();
outPrint = new PrintWriter(out, true);
reader = new Reader(this, clientSocket.getInputStream());
reader.start();
