ServerSocket server = null;
Socket socket=null;
SSLContext ctx;
KeyManagerFactory kmf;
KeyStore ks;
try{
     char[] passphrase = "password".toCharArray();
     String keyfile = "keyName";
     ctx = SSLContext.getInstance("TLS");
     kmf = KeyManagerFactory.getInstance("SunX509");
     ks = KeyStore.getInstance("JKS");
     ks.load(new FileInputStream(keyfile), passphrase);
     kmf.init(ks, passphrase);
     ctx.init(kmf.getKeyManagers(), null, null);
     ServerSocketFactory ssf = ctx.getServerSocketFactory();
     server = ssf.createServerSocket(port);
}catch (IOException e){
     e.printStackTrace();
}               
while (true) {              
     socket = server.accept();
     new Thread(new WorkerThread(socket));                

}
