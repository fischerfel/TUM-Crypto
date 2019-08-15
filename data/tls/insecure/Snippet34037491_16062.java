SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(null, null, null);
server.useHttps(sslContext.getSocketFactory(), false);
