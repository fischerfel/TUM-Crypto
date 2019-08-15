   package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class Server extends ServerSocket {
    public Server() throws IOException {
        super();
        // TODO Auto-generated constructor stub
    }
    public static void main(String args[]) 
    {
        System.setProperty("java.net.ssl.trustStore", "mam.store");
        System.setProperty("java.net.ssl.keyStorePassword", "mamoon");
        try {
            /*KeyStore ks  = KeyStore.getInstance("JKS");
            InputStream in = new FileInputStream("/Users/Ahmed/Desktop/mam.store");
            ks.load(in, "mamoon".toCharArray());
            in.close();
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks,"mamoon".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);
            SSLContext con = SSLContext.getInstance("TLSv1.2");
            con.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            SSLServerSocket socket = (SSLServerSocket)con.getServerSocketFactory().createServerSocket(4040);
            */
            ServerSocket socket = new ServerSocket(4040);
            System.out.println("Server is up.");

            Socket s = socket.accept();
            PrintWriter w = new PrintWriter(s.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream(),"ISO-8859-1"));
            String line;
            String packet = "";
            int l=0;
            while((line = br.readLine()) != null)
            {
                packet += line;
                System.out.println(line.length());
                if(packet.length() >= 100)
                {
                for(char c:packet.toCharArray())
                    System.out.print((int)c + "   ");
                System.out.println();
                new TLS(packet);
                break;
                }
            }
            System.out.println(l);
            //w.write("<html><body>hello world</body> </html>");
            //s.close();
            socket.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
