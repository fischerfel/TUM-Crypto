  private SSLSocketFactory getSocketFactory() 
  {
    KeyStore ks = KeyStore.getInstance("JKS");
    // get user password and file input stream
    char[] password = "prince".toCharArray();
    ClassLoader cl = this.getClass().getClassLoader();
    String location =  "D:\\DEV_HOME\\openapi-samplecode6.jks";
  //  String location =  "openapi-samplecode1.jks";
    System.out.println("location =" + location );
    InputStream stream = cl.getResourceAsStream(location);
    ks.load(stream, password);
   // stream.close();

    SSLContext sc = SSLContext.getInstance("TLS");
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

    kmf.init(ks, password);
    tmf.init(ks);

    sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(),null);

    return sc.getSocketFactory();
  }
