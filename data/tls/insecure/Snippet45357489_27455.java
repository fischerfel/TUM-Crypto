   JAVA SERVER 


    KeyStore ks = KeyStore.getInstance("PKCS12");
    ks.load(new FileInputStream("e:\\myp12file.p12"), "456123".toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, "456123".toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509"); 
    tmf.init(ks);
    SSLContext sc = SSLContext.getInstance("TLS"); 
    TrustManager[] trustManagers = tmf.getTrustManagers(); 
    sc.init(kmf.getKeyManagers(), trustManagers, null); 
   SSLServerSocketFactory ssf = sc.getServerSocketFactory();  
   SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(112);
   System.out.println("S");
   SSLSocket c = (SSLSocket) s.accept();
   int i = c.getInputStream().read();
   c.getOutputStream().write(new String("fdsafdsfdsfgfd").getBytes());
