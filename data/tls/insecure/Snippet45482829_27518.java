 public static void main(String[] args) throws Exception {
      KeyStore ks = KeyStore.getInstance("PKCS12");
      ks.load(TestEcho.class.getResourceAsStream("/localhost.pfx"),
              "****".toCharArray());
      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, "****".toCharArray());
      SSLContext sslc = SSLContext.getInstance("TLS");
      sslc.init(kmf.getKeyManagers(), null, null);

      // try (ServerSocket server = new ServerSocket(4443)) {
      try (ServerSocket server = sslc.getServerSocketFactory().createServerSocket(4443)) {
           while (true) {
                Socket socket = server.accept();
                // ((SSLSocket) socket).getSession();
                socket.getInputStream().read();
                socket.getOutputStream().write("HTTP/1.1 200 OK\r\nContent-Length: 2\r\nConnection: close\r\n\r\nok".getBytes());
                socket.getOutputStream().flush();
           }
      }
 }
