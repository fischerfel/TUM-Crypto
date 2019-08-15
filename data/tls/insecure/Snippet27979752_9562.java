final TrustManager[] trustManager = new TrustManager[] { new MyX509TrustManager() };
final SSLContext context = SSLContext.getInstance("TLS");
context.init(null, trustManager, null);
SSLSocketFactory factory = context.getSocketFactory();
Socket s = factory.createSocket(new Socket(proxy_ip, 3128), hostName, port, true);
