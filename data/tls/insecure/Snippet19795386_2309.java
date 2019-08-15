SSLContext sslContext = SSLContext.getInstance("TLS");
SSLSocketFactory sf = sslContext.getSSLSocketFactory();
Socket socket = new Socket();
Socket sslSocket = sf.createSocket(socket, "ssl.example.com", 4443, true);
OutputStream out = sslSocket.getOutputStream();
out.write(/* ... */);
