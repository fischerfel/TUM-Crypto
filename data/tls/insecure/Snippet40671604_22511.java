// Method to create the socket
private Socket initSocket() throws Exception {
   IO.Options op = new IO.Options();
   try {
      SSLContext sslContext = SSLContext.getInstance("TLS");

      HostnameVerifier hostnameVerifier = new HostnameVerifier() {
          @Override
          public boolean verify(String hostname, SSLSession session) {
              HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
              boolean result = hv.verify(hostname, session);
              return result;
          }
      };

      op.hostnameVerifier = hostnameVerifier;
      op.sslContext = sslContext;
      op.secure = true;
  }
  catch (Exception e) {
      e.printStackTrace();
  }

   op.timeout = SOCKET_TIMEOUT;
   op.reconnection = true;
   op.reconnectionAttempts = 2;
   op.reconnectionDelay = 2000l;
   op.reconnectionDelayMax = 5000l;

   Socket socket = IO.socket(URL, op);
   return socket;
}
