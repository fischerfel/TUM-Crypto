SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, trustAllCerts, new java.security.SecureRandom());
SSLSocketFactory sslsocketfactory = (SSLSocketFactory) sc.getSocketFactory();

Socket s = (SSLSocket) sslsocketfactory.createSocket(someHost, somePort);

OutputStream o = s.getOutputStream();
