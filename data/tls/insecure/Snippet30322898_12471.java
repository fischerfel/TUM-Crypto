         // Install the all-trusting trust manager
         final SSLContext sc = SSLContext.getInstance("SSL");
         sc.init(null, trustAllCerts, new java.security.SecureRandom());
         JSch jsch = new JSch();
         Session session = jsch.getSession("root", "SSH-server", 22);

         session.setSocketFactory(new SocketFactory() {
          Socket tunnel = null;

          public Socket createSocket(String host, int port) throws IOException, UnknownHostException {

              SSLSocketFactory ssf = sc.getSocketFactory();

              // HTTP
              tunnel = ssf.createSocket(System.getProperty("https.proxyHost"), Integer.getInteger("https.proxyPort"));
              tunnel.setKeepAlive(true);

              doTunnelHandshake(tunnel, host, port);
              System.out.println(tunnel + " connect " + tunnel.isConnected());
              return tunnel; // dummy
          }

          public InputStream getInputStream(Socket socket) throws IOException {
              System.out.println(tunnel + " getInputStream " + socket.isConnected());
              return tunnel.getInputStream();
          }

          public OutputStream getOutputStream(Socket socket) throws IOException {
              System.out.println("getOutputStream");
              return socket.getOutputStream();
          }           });

      session.connect();

      try {
          session.setPortForwardingR(3391, "localhost", 3389);
      ....
