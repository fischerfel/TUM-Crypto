    java.security.Security.addProvider(
        new org.bouncycastle.jce.provider.BouncyCastleProvider());

SSLContext sslcontext = SSLContext.getInstance("TLS");
           sslcontext.init(getKeyManagers(), {new DummyTrustManager()}, null);
SocketFactory socketFactory = sslcontext.getSocketFactory();

SSLSocket socket = (SSLSocket) socketFactory.createSocket("127.0.0.1", 8080); 
socket.getSession().getLocalCertificates();
socket.getSession().getPeerHost();
socket.getSession().getPeerPort();
socket.getSession().getPeerCertificates();`
