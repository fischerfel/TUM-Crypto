  System.setProperty("javax.net.debug", "all");
  SSLContext sc = SSLContext.getInstance("TLSv1.2");
  System.setProperty("https.protocols", "TLSv1.2");//Java 8


  TrustManager[] trustAllCerts = { new InsecureTrustManager() };
  sc.init(null, trustAllCerts, new java.security.SecureRandom());
  HostnameVerifier allHostsValid = new InsecureHostnameVerifier();

  client = ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier(allHostsValid).build();
