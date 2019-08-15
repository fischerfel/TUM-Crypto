InputStream in = new DataInputStream(new FileInputStream(new File("server.crt")));
KeyStore ks = KeyStore.getInstance("JKS");
ks.load(null, null);

CertificateFactory cf = CertificateFactory.getInstance("X.509");
X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
in.close();

ks.setCertificateEntry("dts", cert);

char[] newpass = "password".toCharArray();
String name = "mykeystore.ks";
FileOutputStream output = new FileOutputStream(name);
ks.store(output, newpass);

KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, "password".toCharArray());




try{

    System.setProperty("javax.net.ssl.keyStore","mykeystore.ks");
    System.setProperty("javax.net.ssl.keyStorePassword","password");
    System.setProperty("javax.net.debug","all");



    SSLContext context = SSLContext.getInstance("TLSv1.2");
    context.init(kmf.getKeyManagers(), null, null);
    SSLServerSocketFactory sslServerSocketfactory = context.getServerSocketFactory();
    SSLServerSocket sslServerSocket = (SSLServerSocket)sslServerSocketfactory.createServerSocket(8000);
    SSLSocket sslSocket = (SSLSocket)sslServerSocket.accept();  


    InputStream dataIN = sslSocket.getInputStream();

    byte[] hello = new byte[20];

    dataIN.read(hello);
    System.out.println(new String(hello));

    dataIN.close();

} catch (IOException e){
    e.printStackTrace();
}
