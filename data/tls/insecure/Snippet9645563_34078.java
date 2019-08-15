  java.security.Provider provider = new javaxt.ssl.SSLProvider();
  java.security.Security.addProvider(provider);
  SSLContext sslc = SSLContext.getInstance("TLS", "SSLProvider");
