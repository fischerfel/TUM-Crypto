/* Java SSL Client */

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.util.*;

class client {

String message;
ObjectOutputStream out;
ObjectInputStream in;


public static void main(String[] args) {
    new client().run();
}

void run() {

System.out.println();

int port = 9999;

String host = "127.0.0.1";

try {

    // Security test (to avoid starting with the command line)
    char[] passphrase = "trustword".toCharArray();

    KeyStore keystore = KeyStore.getInstance("JKS");
    keystore.load(new FileInputStream("/home/ME/CERT/truststore"), passphrase);

   TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
   tmf.init(keystore);


   SSLContext context = SSLContext.getInstance("TLS");
   TrustManager[] trustManagers = tmf.getTrustManagers();

   context.init(null, trustManagers, null);



    // --- End of security test



    System.out.println("Connecting to " + host + " on port " + port);

    // 1. Create a client socket. 
    SSLSocketFactory sslFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
    SSLSocket sslSocket = (SSLSocket)sslFactory.createSocket(host, port);

    // TBH im not sure what the following code does, but I need it?
    String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
    sslSocket.setEnabledCipherSuites(enabledCipherSuites);


    System.out.println("Connected.");

    // 2. Create input and output streams
    out = new ObjectOutputStream(sslSocket.getOutputStream());
    out.flush();
    in = new ObjectInputStream(sslSocket.getInputStream());

    // 3. Client and server talks via the input and output streams

    Scanner input = new Scanner(System.in);

    message = (String)in.readObject();
    System.out.println(" Server says: " + message);

    sendMessage("Hello Server! ");

    do {
    // Sends input text
    System.out.print("Enter text to send: ");
    message = input.nextLine();
    sendMessage(message);

    if (message.equals("bye")) {
        message = (String)in.readObject();
        System.out.println(" Server says: " + message);
    }

    } while(!message.equals("bye"));

    // 4. Closing connection

    in.close();
    out.close();
    sslSocket.close();
    System.out.println("Client terminated. ");
}

catch (Exception exception) {
    exception.printStackTrace();

}

}

void sendMessage(String msg) throws Exception
{
out.writeObject(msg);
out.flush();
System.out.println("  You(client) says: " + msg);
}
