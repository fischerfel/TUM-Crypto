SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null,null,null);
SSLSocketFactory sf = sc.getSocketFactory();
SSLSocket ss = (SSLSocket)sf.createSocket();
System.out.println(Arrays.toString(ss.getSupportedProtocols()));
