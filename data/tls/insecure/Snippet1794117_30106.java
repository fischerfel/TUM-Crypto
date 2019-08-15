SSLContext sctx1 = SSLContext.getInstance("SSLv3");
sctx1.init(new X509KeyManager[] { 
    new MyKeyManager("/config/master.jks","changeme".toCharArray(),"site1.example.com")
    },null, null);
SSLServerSocketFactory ssf = (SSLServerSocketFactory) sctx1.getServerSocketFactory();
ServerSocket ss1 = ssf.createServerSocket(1234);

...

SSLContext sctx2 = SSLContext.getInstance("SSLv3");
sctx2.init(new X509KeyManager[] { 
    new MyKeyManager("/config/master.jks","changeme".toCharArray(),"site2.example.com") 
    },null, null);
ssf = (SSLServerSocketFactory) sctx2.getServerSocketFactory();
ServerSocket ss2 = ssf.createServerSocket(5678);
