public class SSLServer {
   public static void main(String[] args) {
      String ksName = "key.jks";
      char ksPass[] = "password".toCharArray();
      char ctPass[] = "password".toCharArray();
      try {
         KeyStore ks = KeyStore.getInstance("JKS");
         ks.load(new FileInputStream(ksName), ksPass);
         KeyManagerFactory kmf = 
         KeyManagerFactory.getInstance("SunX509");

         kmf.init(ks, ctPass);
         SSLContext sc = SSLContext.getInstance("TLS");
         sc.init(kmf.getKeyManagers(), null, null);
         SSLServerSocketFactory ssf = sc.getServerSocketFactory();
         SSLServerSocket s 
            = (SSLServerSocket) ssf.createServerSocket(8888);
         System.out.println("Server started:");
         // Listening to the port
         SSLSocket c = (SSLSocket) s.accept();
         BufferedWriter w = new BufferedWriter(
            new OutputStreamWriter(c.getOutputStream()));

         w.write("HTTP/1.0 200 OK");
         w.write("Content-Type: text/html");
         w.write("<html><body>Hello world!</body></html>");
         w.flush();
         w.close();
         c.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

}
