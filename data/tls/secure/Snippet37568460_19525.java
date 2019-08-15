private static void printSSLContext() throws IOException, NoSuchAlgorithmException, KeyManagementException{
     SSLContext context = SSLContext.getInstance("TLSv1.2");
     context.init(null,null,null);

     SSLSocketFactory factory = (SSLSocketFactory)context.getSocketFactory();
     SSLSocket socket = (SSLSocket)factory.createSocket();

     String[] protocols = socket.getSupportedProtocols();

     System.out.println("Supported Protocols: " + protocols.length);
     for(int i = 0; i < protocols.length; i++) {
         System.out.println(" " + protocols[i]);
     }

     protocols = socket.getEnabledProtocols();

     System.out.println("Enabled Protocols: " + protocols.length);
     for(int i = 0; i < protocols.length; i++) {
         System.out.println(" " + protocols[i]);
     }

     String[] ciphers = socket.getSupportedCipherSuites();
     System.out.println("Enabled Ciphers: " + ciphers.length);
     for(int i = 0; i < ciphers.length; i++) {
         System.out.println(" " + ciphers[i]);
     }
    }
