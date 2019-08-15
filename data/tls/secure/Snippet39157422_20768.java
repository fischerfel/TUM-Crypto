context = SSLContext.getInstance("TLSv1.2");
context.init(null,null,null);
SSLContext.setDefault(context); 
SSLSocketFactory factory = (SSLSocketFactory)context.getSocketFactory();
SSLSocket socket = (SSLSocket)factory.createSocket();
protocols = socket.getEnabledProtocols();
