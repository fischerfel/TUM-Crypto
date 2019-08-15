import java.io.FileInputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

public class Server {
  public static void main(String[] args) throws Exception {
    KeyStore ks = KeyStore.getInstance("JKS");  
    ks.load(new FileInputStream("server.jks"), "123456".toCharArray());

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, "123456".toCharArray());

    SSLContext context = SSLContext.getInstance("TLS");
    context.init(kmf.getKeyManagers(), null, null);

    SSLServerSocketFactory factory = context.getServerSocketFactory();
    SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(8443);

    SSLSocket socket = null;
    OutputStream out = null;

    while (true) {
      try {
        System.out.println("Trying to connect");
        socket = (SSLSocket) serverSocket.accept();
        socket.startHandshake();
        out = socket.getOutputStream();
        out.write("Hello World".getBytes());
        out.flush();
      } catch (SSLHandshakeException e) {
        e.printStackTrace();
        continue;
      } finally {
        if (socket != null) {
          socket.close();
        }
      }
    }
  }
}
