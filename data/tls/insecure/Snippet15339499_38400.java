KeyStore ks = KeyStore.getInstance("PKCS12");            
ks.load(new FileInputStream("cert.pfx"),"pass".toCharArray());

System.out.println("init Stores...");

ctx = SSLContext.getInstance("TLS");
ctx.init(new KeyManager[] { new MyKeyManager(ks, "pass") }, trustAllCerts, new SecureRandom());                       
SSLContext.setDefault(ctx);

ProtocolSocketFactory psf = new SSLProtocolSocketFactory();   
Protocol https = new Protocol("https", psf, 443);
Protocol.registerProtocol("https", https);
