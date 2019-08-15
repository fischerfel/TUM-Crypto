import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsExchange;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.Executor;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class HTTPS {
  public static void main(String[] args) throws Exception {
    KeyStore ks = KeyStore.getInstance("JKS");  
    ks.load(new FileInputStream("server.jks"), "123456".toCharArray());

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, "123456".toCharArray());

    SSLContext context = SSLContext.getInstance("TLS");
    context.init(kmf.getKeyManagers(), null, null);

    final HttpsServer server = HttpsServer.create(new InetSocketAddress("localhost", 8443), 10);

    server.createContext("/", new HttpHandler() {
      public void handle(HttpExchange xchng) throws IOException {
        HttpsExchange exchange = (HttpsExchange) xchng;

        String ret = "";
        ret += exchange.getRequestMethod() + " " + exchange.getRequestURI() + " " + exchange.getProtocol() + "\n";

        Headers headers = exchange.getRequestHeaders();
        if (!headers.isEmpty()) {
          ret += "\n";
          for (String key : headers.keySet()) {
            ret += key + ": ";
            boolean semiColon = false;
            for (String value : headers.get(key)) {
              if (semiColon) {
                ret += "; ";
              }

              ret += value;
              semiColon = true;
            }

            ret += "\n";
          }
        }

        if (headers.get("Content-Length") != null) {
          InputStream in = exchange.getRequestBody();
          ret += "\n";
          int i;
          while ((i = in.read()) != -1) {
            ret += String.valueOf((char) i);
          }
        }

        headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "text/plain");

        exchange.sendResponseHeaders(200, ret.length());

        OutputStream out = exchange.getResponseBody();
        out.write(ret.getBytes());

        exchange.close();
      }
    });

    server.setHttpsConfigurator(new HttpsConfigurator(context) {
      public void configure(HttpsParameters params) {

      }
    });

    server.setExecutor(new Executor() {
      public void execute(Runnable command) {
        new Thread(command).start();
      }
    });

    server.start();

    /*
     * In a real app:
     *
     * public class ServerShutdownHook extends Thread {
     *   HttpServer server;
     *   int        seconds;
     *
     *   public ServerShutdownHook(HttpServer server, int seconds) {
     *     this.server  = server;
     *     this.seconds = seconds;
     *   }
     *
     *   public void run() {
     *     System.out.println("Server shutting down.  Waiting " + this.seconds + " seconds for exchanges to complete.");
     *     server.stop(this.seconds);
     *   }
     * }
     *
     * Runtime.getRuntime().addShutdownHook(new ServerShutdownHook(server, 3));
     */
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        System.out.println("Server shutting down.  Waiting 3 seconds for exchanges to complete.");
        server.stop(3);
      }
    });
  }
}
