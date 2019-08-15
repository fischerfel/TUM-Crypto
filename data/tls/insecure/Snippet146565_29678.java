
SSLContext context = SSLContext.getInstance("SSL");
context.init(null, new TrustManager[] { new dummyTrustManager() },
                            new java.security.SecureRandom());

SSLSocketFactory factory = context.getSocketFactory();
InetAddress addr = InetAddress.getByName(host_);
SSLSocket sock =  (SSLSocket)factory.createSocket(addr, port_);
