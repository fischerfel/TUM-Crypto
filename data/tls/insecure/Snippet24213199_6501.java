import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class HttpsServer {
    public static void main(String[] args) {
        String ksName = "myJKS.jks";
        char ksPass[] = "key".toCharArray();
        char ctPass[] = "key".toCharArray();
        try {

//          Runtime.getRuntime().exec("notepad");
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(ksName), ksPass);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, ctPass);
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), null, null);
            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(8888,0, null);

            System.out.println("Server started:");
            printServerSocketInfo(s);
            // Listening to the port
            int count = 0;
            while (true) {
                SSLSocket c = (SSLSocket) s.accept();
                // Someone is calling this server
                count++;
                System.out.println("Connection #: " + count);
                // printSocketInfo(c);
                BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
                        c.getOutputStream()));
                BufferedReader r = new BufferedReader(new InputStreamReader(
                        c.getInputStream()));
                String m = r.readLine();
                System.out.println(m);
//              w.write(m);
                if (m != null) {
                    // We have a real data connection
                    w.write("HTTP/1.1 200 OK");
                    w.newLine();
                    w.write("Content-Type: text/html");
                    //should be two new lines!!!
                    w.newLine();
                    w.newLine();
                    if(m.contains("GET"))
                    //read from file
                    MyFileReader(w,"index.html");

                    while ((m = r.readLine()) != null) {
                        if (m.length() == 0)
                            break; // End of a GET call
//                      w.write(m);
                        System.out.println(m);
                        w.newLine();
                    }
                    w.flush();
                }
                w.close();
                r.close();
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void MyFileReader(BufferedWriter w, String uri) {
        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(uri));

            while ((sCurrentLine = br.readLine()) != null) {
                w.write(sCurrentLine);
                System.out.println(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void printSocketInfo(SSLSocket s) {
        System.out.println("Server socket class: " + s.getClass());
        System.out.println("   Remote address = "
                + s.getInetAddress().toString());
        System.out.println("   Remote port = " + s.getPort());
        System.out.println("   Local socket address = "
                + s.getLocalSocketAddress().toString());
        System.out.println("   Local address = "
                + s.getLocalAddress().toString());
        System.out.println("   Local port = " + s.getLocalPort());
    }

    private static void printServerSocketInfo(SSLServerSocket s) {
        System.out.println("Server socket class: " + s.getClass());
        System.out.println("   Socker address = "
                + s.getInetAddress().toString());
        System.out.println("   Socker port = " + s.getLocalPort());
        System.out.println("   Need client authentication = "
                + s.getNeedClientAuth());
        System.out.println("   Want client authentication = "
                + s.getWantClientAuth());
        System.out.println("   Use client mode = " + s.getUseClientMode());
    }
}
