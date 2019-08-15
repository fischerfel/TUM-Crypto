package com.stackoverflow._19505091;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class AnonTLSExample {

    public static void main(String[] args) throws Exception {

        /* No certs for this example so we are using ECDH_anon exchange. */
        String[] cipherSuites = {"TLS_ECDH_anon_WITH_AES_128_CBC_SHA"};
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");

        /* No certificates, use default secure random source.
         * If we were using authentication (and you should in a real
         * system), this is where we would load 
         * keystores and truststores. */
        sslContext.init(null, null, null);

        /* Create server socket. */
        SSLServerSocket ss = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(12345);
        ss.setEnabledCipherSuites(cipherSuites);

        /*
         * Normally when authentication is used only the client authenticates
         * the server. If you want the server to also authenticate the client
         * set this to true. This will establish bidirectional trust in the session.
         */
        ss.setWantClientAuth(false);

        /* Start server thread. */
        new Thread(new Server(ss), "ServerThread").start();

        /* Create client socket. */
        SSLSocket s = (SSLSocket) sslContext.getSocketFactory().createSocket();
        s.setEnabledCipherSuites(cipherSuites);

        /* Connect to server. */
        System.out.println("Client: Connecting...");
        s.connect(new InetSocketAddress("127.0.0.1", 12345));
        System.out.println("Client: Connected");

        /* Print out some TLS info for this connection. */
        SSLSession session = s.getSession();
        System.out.println("Client: Session secured with P: " + session.getProtocol() + " CS: " + session.getCipherSuite());

        /* Send the secret message. */
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        String message = "Secret Message.";
        System.out.println("Client: Sending: " + message);
        dos.writeUTF(message);

        /* Wait for server to close stream. */
        System.out.println("Client: Waiting for server to close...");
        s.getInputStream().read();

        /* Close client socket. */
        s.close();
        System.out.println("Client: Done.");
    }


}

class Server implements Runnable {

    private final ServerSocket ss;

    public Server(ServerSocket ss){
        this.ss = ss;
    }

    @Override
    public void run() {
        try{
         /* Wait for client to connect. */
         System.out.println("Server: Waiting for connection...");
         Socket s = ss.accept();
         System.out.println("Server: Connected.");

         /* Read secret message. */
         DataInputStream dis = new DataInputStream(s.getInputStream());
         String message = dis.readUTF();
         System.out.println("Server: Received Message: " + message);

         /* Close our sockets. */
         s.close();
         ss.close();
         System.out.println("Server: Done.");
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
