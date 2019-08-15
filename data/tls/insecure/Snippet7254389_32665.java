SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, getAllCerts(), new SecureRandom());
SSLSocketFactory factory = sslContext.getSocketFactory();
mSocket = (SSLSocket) factory.createSocket("myhost.com", socketPort[index]);
