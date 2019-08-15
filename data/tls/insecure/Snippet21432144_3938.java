public static void main(String[] args) throws Exception {
    System.out.println("Opening server socket...");
    System.out.println(System.currentTimeMillis());
    char[] passphrase = "password".toCharArray();
    System.out.println"---------------------");
    System.out.println(java.security.KeyStore.getDefaultType());
    try
    {
        KeyStore keystore1 = KeyStore.getInstance("jks");
        FileInputStream fis = new FileInputStream("mykeystore.jks");
        keystore1.load(fis, passphrase);
        fis.close();
        KeyStore ksTrust = KeyStore.getInstance("jks");
        FileInputStream fis2 =  new FileInputStream("mykeystore.jks");
        ksTrust.load(fis2, passphrase);
        KeyManagerFactory kmf =    KeyManagerFactory.getInstance("SunX509");
        kmf.init(keystore1, passphrase);
        TrustManagerFactory tmf =   TrustManagerFactory.getInstance("SunX509");
        tmf.init(ksTrust);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        SSLServerSocketFactory ssf = 
        (SSLServerSocketFactory) sslContext.getServerSocketFactory();
        SSLServerSocket ss = 
        (SSLServerSocket) ssf.createServerSocket(PORT_WORK);

        while (true) {
            SSLSocket s = (SSLSocket) ss.accept();
            s.startHandshake();
            s.setTcpNoDelay(true);

            byte[]b = new byte[2048];
            s.getInputStream().read(b);
            System.out.println(new String(b).trim());

            s.getOutputStream().write("Test".getBytes("UTF-8"));

            b = new byte[2048];
            s.getInputStream().read(b);
            System.out.println(new String(b).trim());   

            s.close();
            System.out.println("-------||END||-------");
        }
    } catch (Exception e) {
        System.out.println("Cannot open\r\n" + e);
    }
}
