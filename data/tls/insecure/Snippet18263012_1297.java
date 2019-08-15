SSLContext sslcontext = SSLContext.getInstance("TLS");   
sslcontext.init(null, null, null);
SSLSocketFactory factory = sslcontext.getSocketFactory(); 
SSLSocket socket = (SSLSocket)factory.createSocket("myserver", 443);

//socket.startHandshake();
SSLSession session = socket.getSession();
session.getPeerCertificates();
socket.close();
