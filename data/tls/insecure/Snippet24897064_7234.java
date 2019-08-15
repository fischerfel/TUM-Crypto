public static void main(String[] args) {
    String ksName = "mykey.jks";
    char ksPass[] = "mypass".toCharArray();
    char ctPass[] = "mypass".toCharArray();
    try {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(ksName), ksPass);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, ctPass);
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);
        SSLServerSocketFactory ssf = sc.getServerSocketFactory();
        SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(8888,0, null);
    ...[more lines]
}
