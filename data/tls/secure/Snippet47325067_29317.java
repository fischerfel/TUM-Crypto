URL url = new URL("https://www.example.com/test.png");
URLConnection l_connection = null;
SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");
sslcontext.init(null, null, null);
SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
