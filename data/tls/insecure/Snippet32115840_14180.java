System.setProperty("1", "/Library/Java/JavaVirtualMachines/jdk1.8.0_51.jdk/Contents/Home/jre");

String jrehome = System.getProperty("1");
String path = jrehome + "/" + "lib" + "/" + "security" + "/" + "cacerts";
char[] ksPass= "changeit".toCharArray();

try {
    KeyStore ks = KeyStore.getInstance("JKS"); // <- HERE
    System.out.println(ks.toString());
    ks.load(new FileInputStream(path), ksPass);

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, ksPass);

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
    System.out.println(ks.getCertificate("SunX509"));
    tmf.init(ks);

    SSLContext sc = SSLContext.getInstance("TLS");
    sc.getClientSessionContext().setSessionCacheSize(1);
    sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    SSLServerSocketFactory ssf = sc.getServerSocketFactory();

    SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(8888); // <-- HERE

    System.out.println("Server started:");
}
